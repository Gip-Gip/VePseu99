# VePseu99 4/14/2020
the **Ve**ry **Pseu**do 3d renderer for the TI-**99**/4a!

## About

VePseu99 is a port of the original [VePseu](https://github.com/Gip-Gip/VePseu)
to the TI-99/4a. It utilizes the same technology on a less restricted platform
so (hopefully) some good games can actually be made!

## Needed Tools

In order to assemble and run the code I used
[WinAsm99 and Win994a](http://www.99er.net/win994a.shtml) respectively. However,
if you just wish to run the program without installing anything
[{js99er.net](https://js99er.net/#/) works better for emulating than Win994a
and runs in your browser, you'll need a semi-modern computer for it though..

## How To Build

**Windows**
1. Click on concat.bat to load all of the assembly files into one large file
called vepseu.a99
2. Use WinAsm99 or your assembler of choice to assemble vepseu.a99 into a binary
cart file

## VePseuTool

VePseuTool is the map editor for VePseu99! Controls are as follows:

* Leftclick: Place wall
* Rightclick: Options menu
* Scrollwheel Button: Move the grid around with your cursor
* Scrollwheel: Zoom in/out

## Files

* screenshots/* - folder full of screenshots, documenting the engine's
development cycle
* src/ - sourcecode for VePseu99!
* tools/vepseutool.jar - see VePseuTool
* concat.bat - Script that combines all the files under src/ into vepseu.a99
* filetypes.A99.conf - Geany syntax highlights
* LICENSE - text file containing license, important!
* README.md - the file you're reading now!
* template.a99 - simple header template for all .a99 files
* vepseu.a99 - concatination of all the files under src/
* vepseu.bin - binary cart image
* vepseu.obj - cart obj
* wallstyles.png - Texture and pallet info for VePseu

**Contributors**
* [Asmusr](https://atariage.com/forums/profile/35226-asmusr/) - made a
suggestion that brought RNDPSH down to 36,214 cycles! Commits
[6d6249d](https://github.com/Gip-Gip/VePseu99/commit/6d6249d2efa2e430e64105df60e1ee7e93472cfc)
and
[a1b1ad5](https://github.com/Gip-Gip/VePseu99/commit/a1b1ad58f3fa526cafa3411c905633a4ac65de3d)
are to their credit!
