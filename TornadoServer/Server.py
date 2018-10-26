import tornado.escape
import tornado.ioloop
import tornado.web


class VersionHandler(tornado.web.RequestHandler):
    def get(self):
        response = {'test': 'Misha'}
        self.write(response)


class Server():
    def __init__(self):
        self.server = tornado.web.Application([
            (r"/version", VersionHandler)])

    def listen(self):
        self.server.listen(8888)
        tornado.ioloop.IOLoop.instance().start()
