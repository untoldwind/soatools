<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="xml" encoding="UTF-8" indent="no" omit-xml-declaration="yes" />
  <xsl:param name="indent-increment" select="'  '" />
  
  <xsl:template match="*">
    <xsl:param name="indent" select="'&#xA;'" />

    <xsl:value-of select="$indent" /><span class="element">&lt;<xsl:value-of select="name(.)" /></span><xsl:call-template name="namespaces"/><xsl:apply-templates select="@*"/><span class="element">&gt;</span><xsl:apply-templates>
      <xsl:with-param name="indent" select="concat($indent, $indent-increment)" />
    </xsl:apply-templates>
    <xsl:if test="*">
      <xsl:value-of select="$indent" />
    </xsl:if><span class="element">&lt;/<xsl:value-of select="name(.)" />&gt;</span></xsl:template>

  <xsl:template match="@*">
  <xsl:text> </xsl:text><span class="attribute"><xsl:value-of select="name(.)"/>="</span><xsl:value-of select="."/><span class="attribute">"</span></xsl:template>
  
  <!-- WARNING: this is dangerous. Handle with care -->

  <xsl:template match="text()">
    <xsl:value-of select="normalize-space(.)" />
  </xsl:template>
  
  <xsl:template match="comment()">
    <xsl:param name="indent" select="'&#xA;'" />
    
  	<xsl:value-of select="$indent" /><span class="comment"><xsl:text>&lt;!--</xsl:text><xsl:value-of select="normalize-space(.)" /><xsl:text>--&gt;</xsl:text></span>
  </xsl:template>
  
  <!-- Emit namespace declarations -->
  <xsl:template name="namespaces">
	<xsl:for-each select="@*|.">
	<xsl:variable name="my_ns" select="namespace-uri()"/>
	<!-- Emit a namespace declaration if this element or attribute has a namespace and no ancestor already defines it.
		Currently this produces redundant declarations for namespaces used only on attributes. -->
	<xsl:if test="$my_ns and not(ancestor::*[namespace-uri() = $my_ns])">
		<xsl:variable name="prefix" select="substring-before(name(), local-name())"/>
		<xsl:text> </xsl:text><span class='namespace'>xmlns<xsl:if test="$prefix">:<xsl:value-of select="substring-before($prefix, ':')"/></xsl:if>='<xsl:value-of select="namespace-uri()"/>'</span>
	</xsl:if>
	</xsl:for-each>
  </xsl:template>
  
</xsl:stylesheet>