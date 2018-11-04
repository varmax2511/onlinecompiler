import os

import requests
from flask import Flask, render_template, request, session
from flask_socketio import SocketIO

# configuration
game_server = Flask(__name__)
game_server.session_key = str(os.urandom(24))
game_server.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(game_server)

@game_server.route('/result')
def res(request):
    payload = request.POST.get('payload', '')
    payload = json.loads(payload)
    print(payload)
    run_status = payload.get('run_status')
    o = run_status['output']
    print(o)


@game_server.route('/movesp')
def move_sp():
    RUN_URL = u'https://api.hackerearth.com/v3/code/run/'
    CLIENT_ID = '0e115c1546a99d43ad777f01bb67ca21b41c4d37671c.api.hackerearth.com'
    CLIENT_SECRET = '6096669ee59123c5b4fc179c650634db730b03ee'

    source = 'public class Test { public static void main(String[] args) {System.out.println();}}';

    data = {
        'client_id': CLIENT_ID,
        'client_secret': CLIENT_SECRET,
        'async': 0,
        'source': source,
        'lang': "JAVA",
        #'time_limit': 5,
        'memory_limit': 262144
        #'id': 123,
        #'callback': 'http://10.84.24.234:5000/result/'
    }
    
    #data = urllib.urlencode(data)
    r = requests.post(RUN_URL, data=data)
    print(r.json())
    return 'success'

'''
Start server
'''
if __name__ == '__main__':
    if(session and 'user' in session):
        session.pop('user', None)

    socketio.run(game_server, host='10.84.24.234', port=5000)