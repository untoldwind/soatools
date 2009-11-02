<!-- /example/BasicJavaTransformer.xsl -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="xml" encoding="UTF-8" indent="no" />

  <xsl:param name="indent-increment" select="'  '" />


  <xsl:template match="log-messages">
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
      <head>
        <title>Log</title>
        <style type="text/css">
        h1 { color: red }
        table { border: 2px solid black }
        th { background: lightgray }
        td { border: 1px solid black; vertical-align: top }
        td.section { background: lightgray; border: 0px; vertical-align: top }
        span.element { color: blue }
        span.attribute { color: red }
        div.message { border-top: 1px solid black; border-bottom: 1px solid black; margin-bottom: 10px; padding-bottom: 5px; width: 100% }
        </style>        
      </head>
      <body>
        <h1>Summary</h1>
          <nl>
            <xsl:apply-templates select="./*" mode="summary"/>
          </nl>
        <h1>Details</h1>
        <xsl:apply-templates select="./*"/>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="log-message" mode="summary">
    <li>
      <a href="#{position()}">
        <xsl:value-of select="position()"/><xsl:text> </xsl:text><xsl:value-of select="@service-category" /> : <xsl:value-of select="@service-name" />
      </a>
    </li>
  </xsl:template>

  <xsl:template match="log-message[@service-category = 'Navision' and @service-name = 'IncomingGateway']">
    <div class="message">
    <h2><a name="{position()}"><xsl:value-of select="position()"/><xsl:text> </xsl:text><xsl:value-of select="@service-category" /> : <xsl:value-of select="@service-name" />
    </a>
    </h2>
    <xsl:if test="preceding-sibling::log-message">
      <a href="#{position() - 1}">&lt;&lt; Prev</a>
    </xsl:if><xsl:text> </xsl:text>
    <xsl:if test="following-sibling::log-message">
      <a href="#{position() + 1}">Next &gt;&gt;</a>
    </xsl:if>
    <xsl:if test="message/attachments/attachment[@name='original-data']">
      <table>
        <tr><th>Incoming Navision Message</th></tr>
        <tr><td><pre><xsl:value-of select="message/attachments/attachment[@name='original-data']"/></pre></td></tr>
      </table>
      <br/>
    </xsl:if>
    <xsl:apply-templates select="." mode="std" />
    </div>
  </xsl:template>

  <xsl:template match="log-message[@service-category = 'Navision' and @service-name = 'OutgoingGateway']">
    <div class="message">
    <h2><a name="{position()}"><xsl:value-of select="position()"/><xsl:text> </xsl:text><xsl:value-of select="@service-category" /> : <xsl:value-of select="@service-name" />
    </a>
    </h2>
    <xsl:if test="preceding-sibling::log-message">
      <a href="#{position() - 1}">&lt;&lt; Prev</a>
    </xsl:if><xsl:text> </xsl:text>
    <xsl:if test="following-sibling::log-message">
      <a href="#{position() + 1}">Next &gt;&gt;</a>
    </xsl:if>
    <xsl:if test="message/attachments/attachment[@name='original-data']">
      <table>
        <tr><th>Outgoing Navision Message</th></tr>
        <tr><td><pre><xsl:apply-templates select="message/bodies/body[@name='org.jboss.soa.esb.message.defaultEntry']/*" mode="pretty"/></pre></td></tr>
      </table>
      <br/>
    </xsl:if>
    <xsl:apply-templates select="." mode="std" />
    </div>
  </xsl:template>

  <xsl:template match="log-message[@service-category = 'Provisioning' and @service-name = 'StartProcess']">
    <div class="message">
    <h2><a name="{position()}"><xsl:value-of select="position()"/><xsl:text> </xsl:text><xsl:value-of select="@service-category" /> : <xsl:value-of select="@service-name" />
    </a>
    </h2>
    <xsl:if test="preceding-sibling::log-message">
      <a href="#{position() - 1}">&lt;&lt; Prev</a>
    </xsl:if><xsl:text> </xsl:text>
    <xsl:if test="following-sibling::log-message">
      <a href="#{position() + 1}">Next &gt;&gt;</a>
    </xsl:if>
    <table>
      <tr><th colspan="2">Start process</th></tr>
      <tr>
        <td>Process Definition Name</td>
        <td><xsl:value-of select="message/properties/property[@name='jbpm-process-definition-name']/@value"/></td>
      </tr>
      <tr>
        <td>Process Definition Version</td>
        <td><xsl:value-of select="message/properties/property[@name='jbpm-process-definition-version']/@value"/></td>
      </tr>
      <tr>
        <td>Process Definition ID</td>
        <td><xsl:value-of select="message/properties/property[@name='jbpm-process-definition-id']/@value"/></td>
      </tr>
      <tr>
        <td>Process Instance ID</td>
        <td><xsl:value-of select="message/properties/property[@name='jbpm-process-instance-id']/@value"/></td>
      </tr>
    </table>
    <br/>
    <xsl:apply-templates select="." mode="std" />
    </div>
  </xsl:template>

  <xsl:template match="log-message[@service-category = 'Alopa' and @service-name = 'AlopaInvoker']">
    <div class="message">
    <h2><a name="{position()}"><xsl:value-of select="position()"/><xsl:text> </xsl:text><xsl:value-of select="@service-category" /> : <xsl:value-of select="@service-name" />
    </a>
    </h2>
    <xsl:if test="preceding-sibling::log-message">
      <a href="#{position() - 1}">&lt;&lt; Prev</a>
    </xsl:if><xsl:text> </xsl:text>
    <xsl:if test="following-sibling::log-message">
      <a href="#{position() + 1}">Next &gt;&gt;</a>
    </xsl:if>
    <table>
      <tr><th colspan="2">Alopa Request</th></tr>
      <tr>
        <td>Url</td>
        <td><xsl:value-of select="message/attachments/attachment[@name='http-put-url']"/></td>
      </tr>
      <tr>
        <td>Request</td>
        <td><pre><xsl:value-of select="message/attachments/attachment[@name='http-put-request']"/></pre></td>
      </tr>
      <tr>
        <td>Response</td>
        <td><pre><xsl:value-of select="message/attachments/attachment[@name='http-put-response']"/></pre></td>
      </tr>
    </table>
    <br/>
    <xsl:apply-templates select="." mode="std" />
    </div>
  </xsl:template>

  <xsl:template match="log-message[@service-category = 'Pem' and @service-name = 'PemInvoker']">
    <div class="message">
    <h2><a name="{position()}"><xsl:value-of select="position()"/><xsl:text> </xsl:text><xsl:value-of select="@service-category" /> : <xsl:value-of select="@service-name" />
    </a>
    </h2>
    <xsl:if test="preceding-sibling::log-message">
      <a href="#{position() - 1}">&lt;&lt; Prev</a>
    </xsl:if><xsl:text> </xsl:text>
    <xsl:if test="following-sibling::log-message">
      <a href="#{position() + 1}">Next &gt;&gt;</a>
    </xsl:if>
    <table>
      <tr><th colspan="2">Pem Request</th></tr>
      <tr>
        <td>Url</td>
        <td><xsl:value-of select="message/attachments/attachment[@name='http-put-url']"/></td>
      </tr>
      <tr>
        <td>Request</td>
        <td><pre><xsl:value-of select="message/attachments/attachment[@name='http-put-request']"/></pre></td>
      </tr>
      <tr>
        <td>Response</td>
        <td><pre><xsl:value-of select="message/attachments/attachment[@name='http-put-response']"/></pre></td>
      </tr>
    </table>
    <br/>
    <xsl:apply-templates select="." mode="std" />
    </div>
  </xsl:template>

  <xsl:template match="log-message[@service-category = 'Cirpack' and @service-name = 'CirpackInvoker']">
    <div class="message">
    <h2><a name="{position()}"><xsl:value-of select="position()"/><xsl:text> </xsl:text><xsl:value-of select="@service-category" /> : <xsl:value-of select="@service-name" />
    </a>
    </h2>
    <xsl:if test="preceding-sibling::log-message">
      <a href="#{position() - 1}">&lt;&lt; Prev</a>
    </xsl:if><xsl:text> </xsl:text>
    <xsl:if test="following-sibling::log-message">
      <a href="#{position() + 1}">Next &gt;&gt;</a>
    </xsl:if>
    <table>
      <tr><th colspan="2">Cirpack Script</th></tr>
      <tr>
        <td>Commandline</td>
        <td><xsl:value-of select="message/attachments/attachment[@name='command-line']"/></td>
      </tr>
    </table>
    <br/>
    <xsl:apply-templates select="." mode="std" />
    </div>
  </xsl:template>
  
  <xsl:template match="log-message">
    <div class="message">
    <h2><a name="{position()}"><xsl:value-of select="position()"/><xsl:text> </xsl:text><xsl:value-of select="@service-category" /> : <xsl:value-of select="@service-name" />
    </a>
    </h2>
    <xsl:if test="preceding-sibling::log-message">
      <a href="#{position() - 1}">&lt;&lt; Prev</a>
    </xsl:if><xsl:text> </xsl:text>
    <xsl:if test="following-sibling::log-message">
      <a href="#{position() + 1}">Next &gt;&gt;</a>
    </xsl:if>
    <xsl:apply-templates select="." mode="std" />
    </div>
  </xsl:template>
  
  <xsl:template match="log-message" mode="std">
    <table>
      <tr>
        <th colspan="2">Message</th>
      </tr>
      <tr>
        <td>Message ID</td>
        <td>
          <xsl:value-of select="@message-id" />
        </td>
      </tr>
      <tr>
        <td>Correlation ID</td>
        <td>
          <xsl:value-of select="@correlation-id" />
        </td>
      </tr>
      <xsl:apply-templates select="message" mode="std" />
    </table>
  </xsl:template>

  <xsl:template match="message" mode="std">
    <tr>
      <td colspan="2" class="section">Properties</td>
    </tr>
    <xsl:apply-templates select="properties/*" mode="std" />
    <tr>
      <td colspan="2" class="section">Body</td>
    </tr>
    <xsl:apply-templates select="bodies/*" mode="std" />
    <tr>
      <td colspan="2" class="section">Attachments</td>
    </tr>
    <xsl:apply-templates select="attachments/*" mode="std" />
  </xsl:template>

  <xsl:template match="property" mode="std">
    <tr>
      <td>
        <xsl:value-of select="@name" />
      </td>
      <td>
        <xsl:value-of select="@value" />
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="body" mode="std">
    <tr>
      <td>
        <xsl:value-of select="@name" />
      </td>
      <td><xsl:apply-templates select="./text()" mode="pretty"/>
        <xsl:if test="*">
          <pre><xsl:apply-templates select="*" mode="pretty"/></pre>
        </xsl:if>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="attachment" mode="std">
    <tr>
      <td>
        <xsl:value-of select="@name" />
      </td>
      <td><pre><xsl:value-of select="."/></pre>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="text()" mode="std">
  </xsl:template>

  <xsl:template match="*" mode="pretty">
    <xsl:param name="indent" select="'&#xA;'" />

    <xsl:value-of select="$indent" /><span class="element">&lt;<xsl:value-of select="name(.)" /></span><xsl:apply-templates select="@*" mode="pretty"/><span class="element">&gt;</span><xsl:apply-templates mode="pretty">
      <xsl:with-param name="indent" select="concat($indent, $indent-increment)" />
    </xsl:apply-templates>
    <xsl:if test="*">
      <xsl:value-of select="$indent" />
    </xsl:if><span class="element">&lt;/<xsl:value-of select="name(.)" />&gt;</span></xsl:template>

  <xsl:template match="@*" mode="pretty">
  <xsl:text> </xsl:text><span class="attribute"><xsl:value-of select="name(.)"/>="</span><xsl:value-of select="."/><span class="attribute">"</span></xsl:template>
  
  <!-- WARNING: this is dangerous. Handle with care -->

  <xsl:template match="text()" mode="pretty">
    <xsl:value-of select="normalize-space(.)" />
  </xsl:template>

</xsl:stylesheet>