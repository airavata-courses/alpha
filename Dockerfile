FROM python:3

COPY ./requirements.txt /app/requirements.txt

WORKDIR /app

ENV NEWS_API_KEY="2ee42b7d4d0a4575aa73168cdf303854"

RUN pip install -r requirements.txt virtualenv 

COPY . /app

EXPOSE 5000

CMD python /app/app.py

