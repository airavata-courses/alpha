import os
import json
import pprint
from time import sleep
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from kafka import KafkaConsumer


def sendmail(subject, body, from_addr, password, to_addr):
    """
        Sends out email to the string to_addr
        Also requires login details of the email account from which the mail is sent.
    """
    mail = MIMEMultipart()
    mail['From'] = from_addr
    mail['To'] = to_addr
    mail['Subject'] = subject

    funcBody = body + "\n ----------------------------\n You are receiving this email because you are subscribed " \
                      "to notifications from web dashboard"

    mail.attach(MIMEText(funcBody,'plain'))

    server = smtplib.SMTP('smtp.gmail.com',587)
    server.starttls()
    server.login(from_addr, password)
    server.sendmail(from_addr, to_addr, mail.as_string())
    server.quit()
    print("Mail sent successfully to " + to_addr)


if __name__ == '__main__':

    consumer = KafkaConsumer("topic_2", auto_offset_reset='earliest',
                             bootstrap_servers=['localhost:9092'], api_version=(0, 10), consumer_timeout_ms=1000)
    email_password = os.environ["EMAIL_PASSWORD"]

    while True:
        topics = consumer.poll(2000)
        for topic in topics.values():
            for consumer_record in topic:
                cr_data = json.loads(consumer_record.value)
                print(cr_data)
                sendmail("Hourly top News: " + cr_data['news']['title'], cr_data['news']['content'], "siddharthpathak7@gmail.com", email_password, cr_data['toAddr'])

