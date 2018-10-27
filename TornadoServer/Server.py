import requests
import api


URL = 'https://facebook-eyes-naturally.herokuapp.com/update'


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
            r = requests.get(URL).text
            print(r)
            if r:
                self.sendResponse()


s = Server()
s.listen()
print(1)
