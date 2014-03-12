# LumFriend

Trying to get [Friend](https://github.com/cemerick/friend) work with the [LuminusWeb](luminusweb.net) library.


To run the project, just clone and use lein.

    lein ring server.


# Problem

Though I can get friend to work on it's own in a simple project without multiple places to define routes, when used with LuminusWeb, I am not quite sure where to configure Friend.
This is pretty much a default template produced with luminus

    lein new luminus lumfriend

And I'm trying to use basic form authentication. My users are stored in mysql and I will be wiring the login function later.
