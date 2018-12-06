FROM python:3

COPY ./requirements.txt /app/requirements.txt

WORKDIR /app

#ENV NEWS_API_KEY="2ee42b7d4d0a4575aa73168cdf303854"
ARG NEWS_API_KEY
ENV NEWS_API_KEY $NEWS_API_KEY

RUN pip install -r requirements.txt virtualenv 

COPY . /app

EXPOSE 5000

ENTRYPOINT ["python", "/app/app.py"]

