<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="@*|*|processing-instruction()|comment()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*">
				<xsl:sort select="name()"/>
			</xsl:apply-templates>
			<xsl:apply-templates select="*|processing-instruction()|comment()|text()"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
