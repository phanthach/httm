import pandas as pd
import numpy as np
import tensorflow as tf
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder
from tensorflow import keras
from sklearn.metrics import precision_score, recall_score, f1_score, accuracy_score
import os
import json

# Đọc dữ liệu tập huấn luyện và tập kiểm tra
train_data = pd.read_csv("httm/CSV/train.csv")
test_data = pd.read_csv("httm/CSV/test.csv")

# Tiền xử lý nhãn: "mắt mở" -> 1, "mắt nhắm" -> 0
label_encoder = LabelEncoder()
train_data['label'] = label_encoder.fit_transform(train_data['label'])

# Tiền xử lý đặc trưng (feature): Chuyển từ chuỗi thành mảng số
def string_to_array(feature_string):
    return np.array(eval(feature_string))

train_data['feature'] = train_data['feature'].apply(string_to_array)
test_data['feature'] = test_data['feature'].apply(string_to_array)

# Chuyển đổi đầu vào thành mảng NumPy và định dạng lại kích thước
X_train = np.stack(list(train_data['feature'])).reshape(-1, 100, 100, 1)
y_train = train_data['label']
X_test = np.stack(list(test_data['feature'])).reshape(-1, 100, 100, 1)

# Đường dẫn đến thư mục lưu trữ các mô hình
models_dir = "C:\\Users\\Hi\\OneDrive\\Documents\\Coding\\python\\httm\\My_Model"
if not os.path.exists(models_dir):
    os.makedirs(models_dir)
# Đọc hoặc tạo một tệp txt để lưu phiên bản mô hình
model_version_file = "httm\model_version.txt"

if os.path.exists(model_version_file):
    with open(model_version_file, 'r') as f:
        model_version = int(f.read())
else:
    model_version = 1

# Tăng giá trị phiên bản lên 1 để sử dụng cho mô hình mới
model_version += 1

# Lưu giá trị phiên bản mô hình vào tệp txt
with open(model_version_file, 'w') as f:
    f.write(str(model_version))

# Hàm để tạo tên mô hình tự động dựa trên phiên bản
def get_model_name(version):
    return f"model_ver_{version}.h5"

# Tạo tên mô hình tự động dựa trên phiên bản và lưu mô hình
model_name = get_model_name(model_version)
model_path = os.path.join(models_dir, model_name)

# Tạo mô hình
model = keras.Sequential([
    # Lớp tích chập
    keras.layers.Conv2D(32, (3, 3), activation='relu', input_shape=(100, 100, 1)),
    keras.layers.MaxPooling2D((2, 2)),
    keras.layers.Conv2D(64, (3, 3), activation='relu'),
    keras.layers.MaxPooling2D((2, 2)),
    keras.layers.Conv2D(128, (3, 3), activation='relu'),
    keras.layers.MaxPooling2D((2, 2)),
    keras.layers.Flatten(),
    # Lớp kết nối đầy đủ
    keras.layers.Dense(128, activation='relu'),
    keras.layers.Dense(1, activation='sigmoid')  # Sử dụng sigmoid cho bài toán nhị phân
])

# Biên soạn mô hình
model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

# Huấn luyện mô hình
model.fit(X_train, y_train, epochs=10, batch_size=32)

# Dự đoán trên tập kiểm tra
predictions = model.predict(X_test)

# Gắn nhãn vào tập kiểm tra
test_data['predicted_label'] = [1 if pred > 0.5 else 0 for pred in predictions]

test_data['predicted_label'] = test_data['predicted_label'].replace({1: 'mat nham', 0: 'mat mo'})
# Lưu tập test đã được gán nhãn và có cột "predicted_label" đã thay đổi vào file CSV
test_data.to_csv('test_data_daxong.csv', index=False)

# Đọc dữ liệu đã được gán nhãn (test_data_daxong.csv)
test_data = pd.read_csv('test_data_daxong.csv')

# Chuẩn bị nhãn thực tế (true labels) và nhãn dự đoán (predicted labels)
true_labels = test_data['label']
predicted_labels = test_data['predicted_label']

# Tính precision, recall, F1 score và accuracy
precision = precision_score(true_labels, predicted_labels, average='weighted')
recall = recall_score(true_labels, predicted_labels, average='weighted')
f1 = round(f1_score(true_labels, predicted_labels, average='weighted')*100, 2)
accuracy = round(accuracy_score(true_labels, predicted_labels)*100, 2)

# Lưu mô hình vào tệp tin với tên model_name
model.save(model_path)
# In ra tên mô hình và đường dẫn đến tệp mô hình
print(f'{model_name}, {model_path}, {accuracy}')
# Tạo một đối tượng JSON chứa thông tin mô hình
model_info = {
    'Name_model': model_name,  # Tên mô hình
    'Link_model': model_path,  # Đường dẫn tới tệp mô hình
    'F1_Score': f1  # Độ chính xác của mô hình
}

# Lưu thông tin mô hình vào một tệp JSON
json_filename = "httm/model_info.json"
with open(json_filename, 'w') as json_file:
    json.dump(model_info, json_file)

print(f'Model Info JSON saved at: {json_filename}')

