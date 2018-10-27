import tornado.escape
import tornado.ioloop
import tornado.web
import os
#import api


#feed = api.get_feed()


class FeedHandler(tornado.web.RequestHandler):
    def get(self):
        response = ''  # next(feed).encode()
        self.write(response)


class Server():
    def __init__(self):
        self.server = tornado.web.Application([
            (r"/feed", FeedHandler)])

    def listen(self):
        port = int(os.environ.get("PORT", 80))
        self.server.listen(port)
        tornado.ioloop.IOLoop.instance().start()
