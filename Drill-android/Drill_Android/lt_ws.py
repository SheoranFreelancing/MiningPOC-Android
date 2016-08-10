#!/usr/bin/python
#_*_ coding: utf-8 _*_

""" lt_ws.py defines utilities for accessing LivingTree web services """

__author__ = 'sai@livingtree.com'
__copyright__ = "Copyright 2014-16, Snowflake Technologies Inc. USA"

import os,subprocess,ConfigParser,shutil, httplib, sys, base64, urllib, json, datetime


class LTWS:

    __LTWS_APP_VERSION__ = "1.0"
    __LTWS_WS_VERSION__ = "1.6"
    __LTWS_DEVICE_TOKEN__ = "LivingTreeWebServicePythonClient"
    __LTWS_SUCCESS__ = 0
    __LTWS_ERROR__ = -1
    __LTWS_TIMEOUT__ = -2
    __LTWS_TOOSLOW__ = -3

    def __init__(self, config_filename = ''):
        ws_config = ConfigParser.ConfigParser()

        assert config_filename != ''

        ws_config.read(config_filename)

        # Example
        # [ws]
        # username: livingtree-dev@livingtree.com
        # userpwd: *&(*#&&$
        # wsserver: banyan.livingtree.com
        # usehttpauth: False
        # httpauthlogin: none
        # httpauthpwd: none
        self.ws_login = ws_config.get('ws', 'username')
        self.ws_pwd = ws_config.get('ws', 'userpwd')
        self.ws_server = ws_config.get('ws', 'wsserver')
        use_http_auth = ws_config.get('ws', 'usehttpauth')
        http_auth_login = ws_config.get('ws', 'httpauthlogin')
        http_auth_pwd = ws_config.get('ws', 'httpauthpwd')

        assert self.ws_login != ''
        assert self.ws_pwd != ''
        assert self.ws_server != ''

        if 'True' == use_http_auth:
            auth = base64.encodestring('%s:%s' % (http_auth_login, http_auth_pwd)).replace('\n', '')
            http_basic_auth_header = "Basic %s" % auth
            self.headers = {"Content-type": "application/x-www-form-urlencoded",
                       "Accept": "text/plain", "Authorization": http_basic_auth_header}
        else:
            self.headers = {"Content-type": "application/x-www-form-urlencoded",
                       "Accept": "text/plain"}
        self.conn = httplib.HTTPConnection(self.ws_server)


    def __request__(self, params = {}):
        assert len(params)
        start = datetime.datetime.now()
        self.conn.request("POST", "/services/api/rest/json", params, self.headers)
        response = self.conn.getresponse()
        end = datetime.datetime.now()
        return{'response': response, 'latency': end-start}


    def login(self):
        params = urllib.urlencode({
            'method': 'auth.getusertoken',
            'auth_token': '',
            'username': self.ws_login,
            'password': self.ws_pwd,
            'app_version': self.__LTWS_APP_VERSION__,
            'device_token': self.__LTWS_DEVICE_TOKEN__
        })
        result = self.__request__(params)
        response = result['response']
        #print response.status, response.reason
        data = response.read()
        #print data
        response = json.loads(data)
        self.auth_token = response['result']['authentication']['token']
        self.user_id = response['result']['authentication']['userid']
        assert self.auth_token != ''
        assert self.user_id != ''
        assert response['status'] == self.__LTWS_SUCCESS__
        print sys._getframe().f_code.co_name + " | " +  str(response['status']) + " | " + str(result['latency'].seconds) + " seconds"
        return {'status': response['status'], 'latency': result['latency']}

    def logout(self):
        params = urllib.urlencode({
            'method': 'auth.user_logout',
            'auth_token': self.auth_token,
            'userid': self.user_id,
            'device_token': 'testsai1234'
        })

        result = self.__request__(params)
        response = result['response']
        # print response.status, response.reason
        self.conn.close()
        assert response.status == 200
        return_status = self.__LTWS_SUCCESS__ if 200 == response.status else self.__LTWS_ERROR__
        print sys._getframe().f_code.co_name + " | " +  str(return_status) + " | " + str(result['latency'].seconds) + " seconds"
        return {'status': return_status, 'latency': result['latency']}

    def send_message(self, subject='', body='', toList=[]):
        assert self.auth_token != ''
        if (subject == '') or (body == '') or (toList == []):
            return self.__LTWS_ERROR__

        requestDictionary = {}
        requestDictionary['method'] = 'message.send'
        requestDictionary['auth_token'] = self.auth_token
        requestDictionary['subject'] = subject
        requestDictionary['body'] = body
        i = 0
        for item in toList:
            sendItem = 'send_to[%d]' % i
            requestDictionary[sendItem] = str(item)
            i += 1
        requestDictionary['reply'] = 0
        requestDictionary['draft_id'] = 0
        requestDictionary['ltws_version'] = self.__LTWS_WS_VERSION__
        #print requestDictionary
        params = urllib.urlencode(requestDictionary)
        #print params
        result = self.__request__(params)
        response = result['response']
        #print response.status, response.reason
        data = response.read()
        #print data
        response = json.loads(data)
        #print response
        assert response['status'] == 0
        print sys._getframe().f_code.co_name + " | " +  str(response['status']) + " | " + str(result['latency'].seconds) + " seconds"
        return {'status': response['status'], 'latency': result['latency']}

    def post_text(self, text='', visibilitiesList=[]):
        assert self.auth_token != ''
        if (text == '') or (visibilitiesList == []):
            return self.__LTWS_ERROR__

        requestDictionary = {}
        requestDictionary['method'] = 'wire.save_post'
        requestDictionary['auth_token'] = self.auth_token
        requestDictionary['text'] = text
        requestDictionary['container_guid'] = '00'
        requestDictionary['notifyall'] = 'true'
        requestDictionary['aggregation'] = 'mlt'
        requestDictionary['comments_allowed'] = 'true'
        requestDictionary['ltws_version'] = '1.6'
        i = 0
        for item in visibilitiesList:
            sendItem = 'post_access_id[%d]' % i
            requestDictionary[sendItem] = str(item)
            i += 1
        #requestDictionary['post_access_id[]'] = visibilitiesList
        #print requestDictionary
        params = urllib.urlencode(requestDictionary)
        #print params
        result = self.__request__(params)
        response = result['response']
        #print response.status, response.reason
        data = response.read()
        #print data
        response = json.loads(data)
        print response
        assert response['status'] == 0
        print sys._getframe().f_code.co_name + " | " +  str(response['status']) + " | " + str(result['latency'].seconds) + " seconds"
        return {'status': response['status'], 'latency': result['latency']}

    def self_test(self):
        result = self.login()
        if self.__LTWS_SUCCESS__ == result:
            print "Login successful"
        result = self.logout()
        if self.__LTWS_SUCCESS__ == result:
            print "Logout successful"


if __name__ == '__main__':

    if len(sys.argv) < 2:
        print "USAGE: python lt_ws.py <config file name>"
    else:
        config_file_name = sys.argv[1]

    assert config_file_name != ''

    lt_ws = LTWS(config_file_name)
    lt_ws.self_test()



