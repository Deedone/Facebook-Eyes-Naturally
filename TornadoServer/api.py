from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.options import Options
token = "EAAEuomxbKGkBAA0BChKIfZAsjCP4a099SQYRShhp58JTZAhLZCUj9HvdjIEQJjOISaRwY6XpcjIG4A0EhOaNNZC7hv6tAlzP4cPNGUz7uKPWH0PBkL9mUMi0JgQ2tIMILMnyVKaXdl1XH8UddNg4d8bZARSX7K0sV1N8K29qbqnHIVvOlXcweBedo1dD2CGTYvqoCoxZCZAcwZDZD"

chrome_options = webdriver.ChromeOptions()
prefs = {"profile.default_content_setting_values.notifications" : 2}
chrome_options.add_experimental_option("prefs", prefs)
chrome_options.add_argument("start-maximized")

driver = webdriver.Chrome(chrome_options=chrome_options)

def login():
    driver.get("https://www.facebook.com/login")
    mail = driver.find_element_by_id("email")
    pasw = driver.find_element_by_id("pass")

    mail.send_keys("nodus1.5.3@ukr.net")
    pasw.send_keys("nodus1.5.3")
    pasw.send_keys(Keys.RETURN)
    driver.get("https://www.facebook.com/feed")
    driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")


def get_feed():
    posts = driver.find_elements_by_xpath("//div[@role='article']")
    for p in posts:
        yield p.text







