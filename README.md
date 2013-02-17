transient2_fps_test
===================

A quick and dirty, proof of concept Android test app for the data structure and rendering methods of the Desktop Level Editor.  Although class definitions have been moved around, there are almost no structural differences between the [app](https://github.com/petekinnecom/transient2_fps_test/tree/master/src/org/petekinnecom) game code and the [desktop](https://github.com/petekinnecom/transient2_level_editor/tree/master/src/org/petekinnecom) game code.  The controller code has been changed to handle Android's input, and the renderer has changed to use Android's Canvas object.  The Database code had to be rewritten as well.

Without any optimizations, this app is running on my Nexus S at approximately 50 fps.  With this green light, I've shifted focus back to the Desktop editor.  
