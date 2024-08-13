<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simple"
                                       page-height="29.7cm" page-width="21cm"
                                       margin="2cm">
                    <fo:region-body margin="1cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="simple">
                <fo:flow flow-name="xsl-region-body">
                    <!-- Add heading "Invoice" in bold -->
                    <fo:block text-align="center" font-size="20pt" font-weight="bold" margin-bottom="1cm" border-bottom="1pt solid black">
                        Invoice
                    </fo:block>

                    <!-- Add the hotel name below the "Invoice" heading -->
                    <fo:block text-align="center" font-size="14pt" font-weight="bold" margin-bottom="1cm" color="brown" font-family="Times New Roman, serif">
                        PAAKASHALA
                    </fo:block>


                    <fo:block text-align="start" font-weight="bold" font-size="14pt">
                        Customer Name:
                        <fo:inline color="darkgreen">
                        <xsl:value-of select="/menu/customerName"/>
                       </fo:inline>
                    </fo:block>

                    <fo:table border="0.5pt solid black" margin-top="1cm">
                        <!-- Define the number of columns in the table with increased width -->
                        <fo:table-column column-width="4cm" border="0.5pt solid black"/>
                        <fo:table-column column-width="4cm" border="0.5pt solid black"/>
                        <fo:table-column column-width="4cm" border="0.5pt solid black"/>
                        <fo:table-column column-width="4cm" border="0.5pt solid black"/>

                        <fo:table-body>
                            <fo:table-row>
                                <!-- Define the header row with borders, larger font, blue text color, and background color -->
                                <fo:table-cell border="1pt solid black" padding="0.5cm" background-color="lightblue">
                                    <fo:block font-weight="bold" font-size="12pt" color="blue">Name</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" padding="0.5cm" background-color="lightblue">
                                    <fo:block font-weight="bold" font-size="12pt" color="blue">Price</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" padding="0.5cm" background-color="lightblue">
                                    <fo:block font-weight="bold" font-size="12pt" color="blue">Quantity</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" padding="0.5cm" background-color="lightblue">
                                    <fo:block font-weight="bold" font-size="12pt" color="blue">Amount</fo:block>
                                </fo:table-cell>
                            </fo:table-row>

                            <xsl:for-each select="menu/OrderDetailsDTO">
                                <fo:table-row>
                                    <!-- Ensure the number of cells matches the number of columns -->
                                    <fo:table-cell border="0.5pt solid black" padding="0.5cm">
                                        <fo:block><xsl:value-of select="name"/></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="0.5pt solid black" padding="0.5cm">
                                        <fo:block><xsl:value-of select="price"/>Rs</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="0.5pt solid black" padding="0.5cm">
                                        <fo:block><xsl:value-of select="quantity"/></fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="0.5pt solid black" padding="0.5cm">
                                        <fo:block><xsl:value-of select="amount"/>Rs</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </xsl:for-each>
                        </fo:table-body>
                    </fo:table>

                    <!-- Add tax rate and total amount below the table -->
                    <fo:block margin-top="1cm">
                        <fo:inline font-weight="bold">Tax Rate:</fo:inline>
                        <fo:inline><xsl:value-of select="5"/>%</fo:inline>
                    </fo:block>
                    <fo:block margin-top="0.5cm">
                        <fo:inline font-weight="bold">Total Amount:</fo:inline>
                        <fo:inline><xsl:value-of select="concat(sum(menu/OrderDetailsDTO/totalAmount), ' Rs')"/></fo:inline>
                    </fo:block>

                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>
