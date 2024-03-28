from flask import Flask, request, jsonify
import subprocess
import json
import pymysql
import pandas as pd
from datetime import datetime

app = Flask(__name__)

# Thiết lập thông tin cơ sở dữ liệu MySQL
db_config = {
    'host': 'localhost',
    'user': 'root',
    'password': '88888888',
    'database': 'httm'
}
csv_folder_path = 'httm/CSV/'
# Biến cờ để kiểm tra trạng thái model
model_trained = False

@app.route('/train-model', methods=['GET'])
def train_model():
    try:
        connection = pymysql.connect(**db_config)
        # Truy vấn dữ liệu từ bảng traindata
        train_data_query = "SELECT Link_path, Feature, Label FROM train_data"
        train_data_df = pd.read_sql(train_data_query, connection)
        # Lưu dataframe thành file CSV với tên cột mới
        train_data_df.to_csv(csv_folder_path + 'train.csv', index=False, mode='w', header=["link_path", "feature", "label"])

        # Truy vấn dữ liệu từ bảng testdata
        test_data_query = "SELECT Link_path, Feature, Label FROM test_data"
        test_data_df = pd.read_sql(test_data_query, connection)
        # Lưu dataframe thành file CSV với tên cột mới
        test_data_df.to_csv(csv_folder_path + 'test.csv', index=False, mode='w', header=["link_path", "feature", "label"])


        # Chạy script Python
        result = subprocess.check_output(["python", "httm/code_cnn.py"], text=True)

        # Đọc thông tin từ tệp JSON sau khi đã chạy xong script
        with open("httm\model_info.json", "r") as json_file:
            model_info = json.load(json_file)

        # Lấy ngày hiện tại
        current_date = datetime.now().strftime('%Y-%m-%d')

        # Lưu thông tin vào cơ sở dữ liệu
        cursor = connection.cursor()

        # Thêm thông tin vào bảng, bao gồm ngày hiện tại
        insert_query = "INSERT INTO machinelearningmodel (Name_Model, Training_Time, Model_Path, Accuracy) VALUES (%s, %s, %s, %s)"
        cursor.execute(insert_query, (model_info['Name_model'], current_date, model_info['Link_model'], model_info['F1_Score']))

        connection.commit()
        connection.close()
    
        # Trả về "đã hoàn thành"
        return "Đã hoàn thành"
    except Exception as e:
        return str(e), 500

if __name__ == '__main__':
    app.run(host='localhost', port=5000, debug=True)
