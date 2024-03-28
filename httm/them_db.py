import csv
import mysql.connector

# Kết nối đến database
db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="88888888",
    database="httm"
)

cursor = db.cursor()

# Mở file csv và đọc dữ liệu
with open('data_1.csv', 'r') as file:
    reader = csv.reader(file)
    
    # Bỏ qua tiêu đề của file csv
    next(reader)
    
    for row in reader:
        image_link, feature, label = row
        # Thêm dữ liệu vào bảng Data
        cursor.execute("INSERT INTO Data (Link_path, Feature, Label) VALUES (%s, %s, %s)", (image_link, feature, label))
    
    # Xác nhận và commit transaction
    db.commit()

cursor.close()
db.close()
