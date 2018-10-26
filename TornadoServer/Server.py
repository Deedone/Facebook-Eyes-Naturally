import tornado.escape
import tornado.ioloop
import tornado.web


class FeedHandler(tornado.web.RequestHandler):
    def get(self):
        response = {'test': 'Misha'}
        self.write(response)


class Server():
    def __init__(self):
        self.server = tornado.web.Application([
            (r"/feed", FeedHandler)])

    def listen(self):
        self.server.listen(8888)
        tornado.ioloop.IOLoop.instance().start()
