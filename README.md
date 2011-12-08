ConnectBot Opener
=================

This is a simple wrapper around [ConnectBot](http://www.connectbot.org/)
that pops up the IME selection dialog (aka, the keyboard selection screen).
If the [Hacker's Keyboard](http://code.google.com/p/hackerskeyboard/) is
set as the active keyboard, ConnectBot is launched.

I'm using this to less-painfully switch back and forth between the Kindle
Keyboard on my Kindle Fire and the Hacker's Keyboard to ssh.

This app requires the Hacker's Keyboard to be installed, which requires
root if you're on a Kindle Fire.

Building
--------
Add a `local.properties` with `sdk.dir=...` and build away.
