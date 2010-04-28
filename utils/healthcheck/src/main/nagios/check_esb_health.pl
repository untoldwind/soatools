#!/usr/bin/perl -w
#
# check_esb_health - nagios plugin
#
#nagios: -epn
#
use strict;
use LWP;
use XML::LibXML;
use Getopt::Long qw(:config no_ignore_case bundling);
use Digest::HMAC_SHA1;
use URI::Escape qw(uri_escape);

use vars qw($MYSELF %opt %rc %param %rc $OK $WARNING $CRITICAL $UNKNOWN);

#--- RC defines
$OK=0;
$WARNING=1;
$CRITICAL=2;
$UNKNOWN=3;

%rc=(
	label	=> { $OK => "OK", $WARNING => "WARNING", $CRITICAL => "CRITICAL", $UNKNOWN => "UNKNOWN", },
	number	=> { "OK" => $OK, "WARNING" => $WARNING, "CRITICAL" => $CRITICAL, "UNKNOWN" => $UNKNOWN, 
                     "ok" => $OK, "warning" => $WARNING, "critical" => $CRITICAL, "unknown" => $UNKNOWN,
                      "o" => $OK,  "w"      => $WARNING, "c"        => $CRITICAL, "u"       => $UNKNOWN, },
	s2r	=> { 0 => $OK, 2 => $WARNING, 3 => $CRITICAL, 1 => $UNKNOWN, },
	r2s	=> { $OK => 0, $WARNING => 2, $CRITICAL => 3, $UNKNOWN => 1, },
	complement => { $OK => $CRITICAL, $WARNING => $OK, $CRITICAL => $OK, $UNKNOWN => $OK, },
	minimum => { $OK => 0, $WARNING => 1, $CRITICAL => 1, $UNKNOWN => 1, },
	maximum => { $OK => 0, $WARNING => 1, $CRITICAL => 1, $UNKNOWN => 1, },
	list	=> { $OK => [],$WARNING => [],$CRITICAL => [],$UNKNOWN => [], },
	textsev	=> ["ok","unknown","warning","critical"],
	top	=> $OK,
	error	=> [ ],
	output => [ ],
	rc => $UNKNOWN
);

$MYSELF="check_esb_health";

sub process_parameters {
	if (! GetOptions(
		"H|host=s"	=> \$opt{host},
		"P|port=i"      => \$opt{port},
		"s|https" => \$opt{https},
		"A|authorization=s" => \$opt{authorization},
		"S|secret=s" => \$opt{secret},
		"n|name=s"	=> \$opt{name},
		"t|timeout=i" => \$opt{timeout},
		"d|debug" => \$opt{debug},
		"h|help"	=> \$opt{help},
		)
	) {
		short_usage();
        	return 1;
	}

	if ($opt{help}) {
		short_usage();
        	long_usage();
        	return 1;
	}
	if (!$opt{host}) {
        	print "$MYSELF error: no host specified\n";
		short_usage();
        	return 1;
	} else {
		$param{host}=$opt{host};
	}
	if ($opt{authorization}) {
	  $param{authorization} = $opt{authorization};
	} else {
	  $param{authorization} = 'Local-Developer';
	}
	if ($opt{secret}) {
	  $param{secret} = $opt{secret};
	} else {
	  $param{secret} = '';
	}
	
	if ($opt{port}) {
	  $param{port} = $opt{port};
	}
	
	if ($opt{name}) {
	  $param{name} = $opt{name};
	} else {
	  $param{name} = '';
	}
	
	if ($opt{timeout}) {
	  $param{timeout} = $opt{timeout};
	} else {
	  $param{timeout} = 5000;
	}

	if ($opt{https}) {
	  $param{method} = 'https';
	} else {
	  $param{method} = 'http';
	}
	
	$param{debug} = $opt{debug};
	
	return 0;
}

sub short_usage {
print <<SHORTEOF;

$MYSELF -H <host> -P <port> -A <authorization> -S <secret> -t <timout> -d -s -n <name>
$MYSELF [-h | --help]
SHORTEOF
}

sub long_usage {
print <<LONGEOF;

Options:
-H, --host <host>
  hostname
-P, --port <port>
  port number (default: 80)
-A, --authorization <authorization>
  static authorization string (deprecated)
-S, -secret <secret>
  hashed based authorization secret (use this instead of -A)
-s, --https
  use https instead of http
-t, --timeout
  timeout in milliseconds (default: 5000)
-d, --debug
  enable debug messages
-h, --help
  this help

LONGEOF
}

sub add_error {
	push @{$rc{error}}, @_;
}

sub add_output {
	push @{$rc{output}}, @_;
}

sub do_analyse {
	my $element = shift;
	my $state = $OK;
	my $substate;
	
	foreach my $subelement ($element->getChildrenByTagName('*')) {
		if ( $subelement->nodeName eq 'error' ) {
			add_output("CRITICAL " . $subelement->testContent);
			$state = $CRITICAL;
		} elsif ( $subelement->hasAttribute('state') ) {
			if ($state < $WARNING && $subelement->getAttribute('state') =~ /WARN/i) {
				add_output("WARN " . $subelement->nodeName);
				$state = $WARNING;
			} 
			if ($state < $CRITICAL && $subelement->getAttribute('state') =~ /ERROR/i) {
				add_output("ERROR " . $subelement->nodeName);
				$state = $CRITICAL;
			}
			
			$substate = do_analyse($subelement);
			
			if ( $substate > $state ) {
				$state = $substate;
			}
		} else {
			if ($state < $WARNING && $subelement->textContent =~ /WARN/i) {
				add_output("WARN " . $subelement->nodeName);
				$state = $WARNING;
			} 
			if ($state < $CRITICAL && $subelement->textContent =~ /ERROR/i) {
				add_output("ERROR " . $subelement->nodeName);
				$state = $CRITICAL;
			}
		}
	}
	
	return $state;
}

sub do_query {
	my $parser = XML::LibXML->new;
	my $browser = LWP::UserAgent->new;
	my $host = $param{host};
	
	
	if ( $param{port} ) {
  		$host = $host . ':' .  $param{port}
	}
	my $url;
	
	if ( $param{secret} ) {
		my $hmacdata = '/healthcheck?expire=' . ( time + 300 ) ;
		my $hmac = Digest::HMAC_SHA1->new($param{secret});	
		
		$hmac->add($hmacdata);
		
		$url = "$param{method}://$host$hmacdata&timeout=$param{timeout}&signature=" . uri_escape($hmac->b64digest);
	} else {
		$url = "$param{method}://$host/healthcheck?timeout=$param{timeout}&Authorization=$param{authorization}";
	}
	
	if ($param{debug}) {
		print STDERR  $url, "\n";
	}
	
	my $response = $browser->get($url);
	
	if ( $response->code != 200 ) {
		add_output('Http error: ' . $response->code);
		$rc{rc} = $CRITICAL;
		return;
	}

	if ( $response->content_type ne 'application/xml' ) {
		add_output('Invalid content type: ' .  $response->content_type);
		$rc{rc} = $CRITICAL;
		return;
	}

	if ($param{debug}) {	
		print STDERR "---\n" . $response->content . "\n---\n";
	}
	
	my $document = $parser->parse_string($response->content);
	my $healthcheck = $document->documentElement();

	if ($healthcheck->nodeName ne 'healthcheck') {
		add_output('Invalid content: ' .  $healthcheck->nodeName);
		$rc{rc} = $CRITICAL;
		return;
	}
	
	$rc{rc} = do_analyse($healthcheck)
}

sub mysubst {
	my ($src,$pattern,$substitution)=@_;
	$src=~s/$pattern/$substitution/g;
	return $src;
}

sub do_report {
	my $report_output="";
	
	$report_output=sprintf "%s%s - %s", 
			($param{name}) ? "$param{name} " : "",
			$rc{label}{$rc{rc}},
			join(", ", @{$rc{output}});
			
	#--- add errors if encountered
	$report_output.=" [" . join(", ",@{$rc{error}}) . "]" if (defined($rc{error}[0]));
	
	#--- replace all '|' with PIPE
	print mysubst($report_output,"\\|","PIPE");
	
	printf "\n";
}

if (&process_parameters != $OK) {
	exit $UNKNOWN;
}

#--- don't run this as root ;-)
add_error("please don't run plugins as root!") if ($> == 0);

#--- execute command
&do_query();

#--- report
&do_report();

#--- return rc with highest severity
exit $rc{rc};

