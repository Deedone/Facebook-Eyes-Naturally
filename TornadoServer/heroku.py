import tornado.escape
import tornado.ioloop
import tornado.web
import os


task = ''
response = ''
flag = False


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
        global flag
        global response
        response = self.request.body
        flag = True


class taskHandler(tornado.web.RequestHandler):
    def get(self):
        global task
        self.write(task)
        task = ''


def send_response(handler):
    global response
    global flag
    if flag:
        handler.write(response)
        flag = False


application = tornado.web.Application([
    (r"/feed", FeedHandler),
    (r"/msg", MSGHandler),
    (r"/update", taskHandler),
    (r"/set_response", ResponseHandler),
])

if __name__ == "__main__":
    port = int(os.environ.get("PORT", 8887))
    application.listen(port)
    tornado.ioloop.IOLoop.instance().start()
