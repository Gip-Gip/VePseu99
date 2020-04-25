# VePseu99 4/25/2020
the **Ve**ry **Pseu**do 3d renderer for the TI-**99**/4a!

## About

VePseu99 is a port of the original [VePseu](https://github.com/Gip-Gip/VePseu)
to the TI-99/4a. It utilizes the same technology on a less restricted platform
so (hopefully) some good games can actually be made!

## Needed Tools

In order to assemble the code you'll need
[xas88](https://endlos99.github.io/xdt99/) and to emulate I use
[js99er.net](https://js99er.net/#/).

## How To Build

**Windows**
1. Click on concat.bat to load all of the assembly files into one large file
called vepseu.a99
2. Use XAS99 to assemble vepseu.a99 into a binary
cart file

**Linux**
1. Run make
2. Use XAS99 to assemble vepseu.a99 into a binary

## Controls

* W: Move forward
* S: Move backward
* A: Turn left
* D: Turn right
* 1-8: interact

## VePseuTool

VePseuTool is the map editor for VePseu99! Controls are as follows:

* Leftclick: Place wall
* Rightclick: Options menu
* Scrollwheel Button: Move the grid around with your cursor
* Scrollwheel: Zoom in/out
* Shift+Leftclick: Remove wall
* S: Place scene
* Shift+S: Remove scene
* Ctrl+S: Save
* E: Edit scene(puts you in scene mode)

**Scene Mode**

* A: Rotate scene counterclockwise
* D: Rotate scene clockwise
* S: Place sprite at cursor
* Shift+S: Remove sprite
* Leftclick: Drag sprite
* Up/Down/Left/Right keys: Fine sprite position adjustment

**Supported Filetypes**

* Maps: VePseuTool-generated assembly files
* Sprites: 8x8 monochrome PNG files(if the background is tranparent make sure
it's transparent black, as in the RGB channels are also set to 0)

## Files

* screenshots/* - folder full of screenshots, documenting the engine's
development cycle
* src/ - sourcecode for VePseu99!
* tools/vepseutool.jar - see VePseuTool
* concat.bat - Script that combines all the files under src/ into vepseu.a99
* filetypes.A99.conf - Geany syntax highlights
* LICENSE - text file containing license, important!
* Makefile - linux makefile
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
