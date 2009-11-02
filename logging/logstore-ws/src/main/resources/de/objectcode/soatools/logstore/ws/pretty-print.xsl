<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="xml" encoding="UTF-8" indent="no" omit-xml-declaration="yes" />
  <xsl:param name="indent-increment" select="'  '" />

  <xsl:template match="*">
    <xsl:param name="indent" select="'&#xA;'" />

    <xsl:value-of select="$indent" /><span class="element">&lt;<xsl:value-of select="name(.)" /></span><xsl:apply-templates select="@*"/><span class="element">&gt;</span><xsl:apply-templates>
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
</xsl:stylesheet>