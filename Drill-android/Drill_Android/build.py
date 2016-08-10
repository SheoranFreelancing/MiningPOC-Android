__author__ = 'Krishna Sai'
__date__ = '27 Nov 2014'

import os,subprocess,ConfigParser,shutil, httplib, sys, base64, urllib, json, datetime
import lt_ws

HTML_INDEX = """
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<title>LivingTree 1.5 - Beta Release</title>
<style type="text/css">
body {background:#fff;margin:0;padding:0;font-family:arial,helvetica,sans-serif;text-align:center;padding:10px;color:#333;font-size:16px;}
#container {width:300px;margin:0 auto;}
h1 {margin:0;padding:0;font-size:14px;}
p {font-size:13px;}
.link {background:#ecf5ff;border-top:1px solid #fff;border:1px solid #dfebf8;margin-top:.5em;padding:.3em;}
.link a {text-decoration:none;font-size:15px;display:block;color:#069;}
.last_updated {font-size: x-small;text-align: right;font-style: italic;}
.created_with {font-size: x-small;text-align: center;}
</style>
</head>
<body>

<div id="container">



<h1>Android Users</h1>

<p><strong>Turn on install of downloaded non-store apps on the security settings in your android phone</strong><br />
<p><strong>You can view the progress of the download on the notifications bar on the top of your screen. Click on the downloaded file to install</strong><br />
Make sure you're visiting this page on your device, not your computer.</p>

<div class="link"><a href="http://banyan.livingtree.com/downloads/android-qa/LivingTree.apk">Tap Here to Install<br />LivingTree Android %s<br />Directly On Your Device</
a></div>

<p><strong>Link didn't work?</strong><br />
Make sure you're visiting this page on your device, not your computer.</p>
</div>

</body>
</html>
"""

__TODAY_STR__ = datetime.datetime.now().date().strftime("%Y-%m-%d")


if __name__ == '__main__':
    # config object
    config = ConfigParser.ConfigParser()
    config.read("./config.ini")

    if len(sys.argv) != 4:
        print "USAGE: python build.py app_version new_revision old_revision"
        exit(-1)

    hg_pull = "hg pull"
    result = os.system(hg_pull)

    app_version = sys.argv[1]
    new_revision = sys.argv[2]
    old_revision = sys.argv[3]

    hg_update_clean = "hg update -C -r " + new_revision
    result = os.system(hg_update_clean)

    hg_rev_log = "hg log -r %s:%s" %(str(new_revision), str(old_revision))
    hg_rev_log_result = os.popen(hg_rev_log).read()



    build_info = app_version + "Rev " + new_revision + " " + __TODAY_STR__

    hg_rev_log_result = "<b>New Android build: " + build_info + "</b>\n\n" + hg_rev_log_result

    build_directory = config.get('build', 'project_location')
    print "Building..." + build_directory
    output = subprocess.check_output("ant clean debug", cwd=build_directory,shell=True)
    print(output)
    if output.find("BUILD SUCCESSFUL"):
        print hg_rev_log_result

        scp_string = "scp bin/LivingTree-debug.apk ubuntu@61.8.155.42:/var/www/downloads/android-qa/LivingTree.apk"
        print scp_string
        update_result = os.system(scp_string)

        index_file_string = HTML_INDEX % build_info
        file = open("index.html", "w")
        file.write(index_file_string)
        file.close()
        scp_string = "scp index.html ubuntu@61.8.155.42:/var/www/downloads/android-qa/index.html"
        update_result = os.system(scp_string)


        ltws = lt_ws.LTWS("config.ini")
        ltws.login()
        # ltws.post_text(hg_rev_log_result, ["5102|16094"])
        ltws.logout()
        pass
