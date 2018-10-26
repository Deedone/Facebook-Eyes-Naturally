import tornado.escape
import tornado.ioloop
import tornado.web
import os
import api


class FeedHandler(tornado.web.RequestHandler):
    def get(self):
        response = api.get_feed()
        self.write(response)


class Server():
    def __init__(self):
        self.server = tornado.web.Application([
            (r"/feed", FeedHandler)])

    def listen(self):
        port = int(os.environ.get("PORT", 8888))
        self.server.listen(port)
        tornado.ioloop.IOLoop.instance().start()
