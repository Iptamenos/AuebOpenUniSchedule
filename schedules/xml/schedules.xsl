<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/schedule">
<html>

<!-- COPY THIS FILE INSIDE THE DIRECTORY "schedules/xml/" -->
<body>

	<xsl:for-each select="department">
		
		<p>
			ΤΜΗΜΑ: <xsl:value-of select="@name"/>
		</p>

		<table border="1">
			<tr>
				<th>MΑΘΗΜΑ</th>
				<th>ΕΞΑΜΗΝΟ</th>
				<th>ΚΑΘΗΓΗΤΗΣ</th>
				<th>ΣΧΟΛΙΑ</th>
				<th>ΩΡΑ</th>
				<th>ΑΙΘΟΥΣΑ</th>
				<th>ΗΜΕΡΑ</th>
			</tr>
			
			<xsl:for-each select="lesson">
				<tr>
					<td><xsl:value-of select="@title"/></td>
					<td><xsl:value-of select="semester"/></td>
					<td><xsl:value-of select="professor"/></td>
					<td><xsl:value-of select="comments"/></td>
					<td><xsl:value-of select="time"/></td>
					<td><xsl:value-of select="room"/></td>
					<td><xsl:value-of select="day"/></td>
				</tr>
			</xsl:for-each>
			
		</table>
		
		<br/>

	</xsl:for-each>
	
</body>

</html>
</xsl:template>
</xsl:stylesheet>
