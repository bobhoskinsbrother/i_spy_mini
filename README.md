What is This
============

I've been playing about with I Spy Tank Mini, which is basically an IP Camera, and wifi router on wheels.
After some wireshark action, I've reverse engineered some basic commands.
I've really checked this into github from my ubuntu machine so I can now check it out on my macbook.

Note, this won't work unless you connect your laptop / desktop, via wifi to the I Spy Tank Mini.



How to Build
============

From the root folder

```
> ant
> cd ./dist
> unzip i_spy_tank_<date>.zip
> ./run_app.sh
```