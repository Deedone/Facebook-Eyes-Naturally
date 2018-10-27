import tornado.escape
import tornado.ioloop
import tornado.web

task = ''
response = ''


class FeedHandler(tornado.web.RequestHandler):
    def get(self):
        task = {'task': 'feed'}
        send_response(self)


class MSGHandler(tornado.web.RequestHandler):
    def get(self):
        task = {'task': 'msg'}
        send_response(self)


class NotifHandler(tornado.web.RequestHandler):
    def get(self):
        task = {'task': 'notifications'}
        send_response(self)


class ResponseHandler(tornado.web.RequestHandler):
    def post(self):
        response = self.request.body


class taskHandler(tornado.web.RequestHandler):
    def post(self):
        global task
        self.write(task)
        task = ''


def send_response(handler):
    global response
    while not response:
        pass
    handler.write(response)
    response = None


application = tornado.web.Application([
    (r"/feed", FeedHandler),
    (r"/msg", MSGHandler)
])

if __name__ == "__main__":
    application.listen(8887)
    tornado.ioloop.IOLoop.instance().start()
