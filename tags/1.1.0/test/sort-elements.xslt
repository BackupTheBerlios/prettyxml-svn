<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<!--xsl:strip-space elements=""/-->
	<xsl:template match="*|@*|processing-instruction()|comment()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|processing-instruction()|comment()|text()"/>
			<xsl:apply-templates select="*">
				<xsl:sort select="name()"/>
				<xsl:sort select="concat(name(@*[1]),@*[1])"/>
				<xsl:sort select="concat(name(@*[2]),@*[2])"/>
				<xsl:sort select="concat(name(@*[3]),@*[3])"/>
				<xsl:sort select="concat(name(@*[4]),@*[4])"/>
				<xsl:sort select="concat(name(@*[5]),@*[5])"/>
				<xsl:sort select="concat(name(@*[6]),@*[6])"/>
				<xsl:sort select="."/>
			</xsl:apply-templates>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
