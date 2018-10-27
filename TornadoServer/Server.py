import requests
import api


URL = 'http://localhost:8887/update'


class Server():
    def __init__(self):
        pass

    def sendResponse(self):
        data = ''
        for s in api.get_feed():
            data += s.text
            requests.post(URL, data)

    def listen(self):
        while True:
            r = requests.get(URL)
            print(r.json())
            if r:
                self.sendResponse()


if __name__ == 'main':
    s = Server()
    s.listen()
