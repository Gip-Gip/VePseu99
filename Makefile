all:
	if test -f vepseu.a99; then rm vepseu.a99; fi
	cat src/main.a99 >> vepseu.a99;
	cd src; find -name "*.a99" \! -name "main.a99" -exec cat '{}' >> ../vepseu.a99 ';'
	echo "END .PRINT (>7000-END), ' BYTES LEFT IN ROM'" >> vepseu.a99;
	echo "    END" >> vepseu.a99;
	echo "Now use xas99 to assemble vepseu.a99!"
