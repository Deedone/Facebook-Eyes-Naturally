import tornado.escape
import tornado.ioloop
import tornado.web
import os
import api


class FeedHandler(tornado.web.RequestHandler):
    def get(self):
        response = ''
        for s in api.get_feed():
            response += s.text
            print(s.text)
        self.write(response)


class Server():
    def __init__(self):
        self.server = tornado.web.Application([
            (r"/feed", FeedHandler)])

    def listen(self):
        port = int(os.environ.get("PORT", 8888))
        self.server.listen(port)
        tornado.ioloop.IOLoop.instance().start()
