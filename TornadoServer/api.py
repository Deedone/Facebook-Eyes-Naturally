from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.options import Options
token = "EAAEuomxbKGkBAA0BChKIfZAsjCP4a099SQYRShhp58JTZAhLZCUj9HvdjIEQJjOISaRwY6XpcjIG4A0EhOaNNZC7hv6tAlzP4cPNGUz7uKPWH0PBkL9mUMi0JgQ2tIMILMnyVKaXdl1XH8UddNg4d8bZARSX7K0sV1N8K29qbqnHIVvOlXcweBedo1dD2CGTYvqoCoxZCZAcwZDZD"

chrome_options = webdriver.ChromeOptions()
#chrome_options.add_argument('headless')
prefs = {"profile.default_content_setting_values.notifications": 2}
chrome_options.add_experimental_option("prefs", prefs)
#chrome_options.add_argument("start-maximized")

driver = webdriver.Chrome(chrome_options=chrome_options)

import time
def login():
    driver.get("https://www.facebook.com/login")
    mail = driver.find_element_by_id("email")
    pasw = driver.find_element_by_id("pass")

    mail.send_keys("nodus1.5.3@ukr.net")
    pasw.send_keys("nodus1.5.3")
    pasw.send_keys(Keys.RETURN)
    driver.get("https://www.facebook.com/feed")
    driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
    time.sleep(1)
    driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
    time.sleep(1)
    driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
    time.sleep(1)
    driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
    time.sleep(1)
    driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
    time.sleep(1)
    driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
    time.sleep(1)
    driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")

    links = driver.find_elements_by_xpath("//a[contains(@class,'see_more_link')]")
    for l in links:
        driver.execute_script("arguments[0].click();", l)
    
    mesbut = driver.find_element_by_xpath("//a[@data-tooltip-content='Messages']")
    mesbut.click()


def get_feed():
    posts = driver.find_elements_by_xpath(
        "//div[@role='article']")
    txt = []
    print("Got "+str(len(posts)) + " posts")
    for p in posts:
        try:
            l = p.find_element_by_xpath(".//a[contains(@class,'profileLink')]")
            m = p.find_element_by_xpath(".//div[@data-ad-preview='message']")
        except Exception:
            print("BAD", p.text)
            continue
        txt.append(l.text + "\n" + m.text + '\n')

    txt2 = []
    return txt


def get_unread_msg():
    elems = driver.find_elements_by_xpath("//ul[contains(@class,'jewelContent')]/li[contains(@class,'jewelItemNew')]")
    s = ""
    for m in elems:
        s += ('\n'.join(m.text.split('\n')[0:2])).replace('(',' ').replace(')',' ')
        s+= '\n'
    return s



login()
print('Success!')
