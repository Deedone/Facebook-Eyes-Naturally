import requests
import api


URL = 'https://facebook-eyes-naturally.herokuapp.com/update'
URL1 = 'https://facebook-eyes-naturally.herokuapp.com/set_response'


class Server():
    def __init__(self):
        pass

    def sendResponse(self):
        data = ''
        for s in api.get_feed():
            data += s.text
            requests.post(URL1, data.encode())
            print('Sent')

    def listen(self):
        while True:
            r = requests.get(URL).text
            if r:
                print(r)
                self.sendResponse()


s = Server()
s.listen()
