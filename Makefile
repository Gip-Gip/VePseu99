all:
	if test -f vepseu.a99; then rm vepseu.a99; fi
	cat src/main.a99 >> vepseu.a99;
	cd src; find -name "*.a99" \! -name "main.a99" -exec cat '{}' >> ../vepseu.a99 ';'
	echo "END .PRINT (>7000-END), ' BYTES LEFT IN ROM'" >> vepseu.a99;
	echo "    AORG >7FFF" >> vepseu.a99;
	echo "    BYTE >00" >> vepseu.a99;
	echo "    END" >> vepseu.a99;
	xas99 -b vepseu.a99 -o vepseu.bin
