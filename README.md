# VePseu99 4/6/2020
the **Ve**ry **Pseu**do 3d renderer for the TI-**99**/4a!

## About

VePseu99 is a port of the original [VePseu](https://github.com/Gip-Gip/VePseu)
to the TI-99/4a. It utilizes the same technology on a less restricted platform
so (hopefully) some good games can actually be made!

## Needed Tools

In order to assemble and run the code I used
[WinAsm99 and Win994a](http://www.99er.net/win994a.shtml) respectively

## How To Build

Open WinAsm99 and add all the files(in the order specified below) to the
project. Select both the produce hex and produce bin checkboxes and
choose the files you want to save both as. Hit assemble and hopefully everything
works!

## Files

**Assembly Files(in order)**

1. main.a99 - code that initializes the ti and establishes a game loop
2. keyboard.a99 - code used to take and utilize input from the keyboard
3. render.a99 - code used to render a picture to the VDP
4. maps.a99 - map data
5. tables.a99 - multiple collections of important non-code data
6. ram.a99 - general ram layout for the upper 4k of the cart
7. end.a99 - code and data that must be placed at the end of the code

**Misc Files**

* screenshots/* - folder full of screenshots, documenting the engine's
development cycle
* filetypes.A99.conf - Geany syntax highlights
* LICENSE - text file containing license, important!
* main.bin - binary cart image
* main.obj - cart obj
* README.md - the file you're reading now!
* template.a99 - simple header template for all .a99 files

**Contributors**
* [Asmusr](https://atariage.com/forums/profile/35226-asmusr/) - made a
suggestion that brought RNDPSH down to 39,264 cycles! Commit
[6d6249d](https://github.com/Gip-Gip/VePseu99/commit/6d6249d2efa2e430e64105df60e1ee7e93472cfc)
is to their credit!
