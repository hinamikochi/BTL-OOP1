package com.example.testbtl2.ui;

import java.util.List;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import com.example.testbtl2.models.Book;
import com.example.testbtl2.models.User;
import com.example.testbtl2.models.Member;
import com.example.testbtl2.models.Employee;
import com.example.testbtl2.models.Document;import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import com.example.testbtl2.models.BorrowedInfo;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LibraryUI extends Application {

    private Stage primaryStage;

    // Đảm bảo bạn khai báo documentList với kiểu ObservableList<Book>
    private ObservableList<Book> documentList = FXCollections.observableArrayList();
    private ObservableList<User> userList = FXCollections.observableArrayList();

    private List<BorrowedInfo> borrowedInfoList = new ArrayList<>();

    private List<User> users = new ArrayList<>();
    private List<Document> documents = new ArrayList<>();

    private List<User> usersList = new ArrayList<>();
    private List<Document> documentsList = new ArrayList<>();

    // Phương thức kiểm tra người dùng đã mượn tài liệu hay chưa
    private boolean userHasBorrowed(User user, Document document) {
        // Kiểm tra xem người dùng có mượn tài liệu này không
        // Giả sử bạn có cách lưu trữ thông tin mượn của người dùng (ví dụ: danh sách mượn của người dùng)
        // Ví dụ dưới đây chỉ mang tính chất giả định, bạn cần thay đổi theo cách bạn tổ chức dữ liệu trong ứng dụng của mình
        for (BorrowedInfo borrowedInfo : borrowedInfoList) {
            if (borrowedInfo.getName().equals(user.getName()) && borrowedInfo.getBookTitle().equals(document.getTitle())) {
                return true; // Nếu tên người mượn và tiêu đề sách khớp, người dùng đã mượn sách này
            }
        }
        return false; // Nếu không tìm thấy, người dùng chưa mượn sách này
    }


    private static final String FILE_PATH = "documents.json"; // Đường dẫn file lưu trữ dữ liệu

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Đọc dữ liệu từ file khi khởi động
        loadDocumentsFromFile();

        // Tạo giao diện chính
        primaryStage.setTitle("Library Management");
        Label welcomeLabel = new Label("Welcome to the Library Management System!");

        Button addDocumentButton = new Button("Add Document");
        Button viewDocumentsButton = new Button("View Documents");
        Button addUserButton = new Button("Add User");
        Button borrowDocumentButton = new Button("Borrow Document");
        Button showUsersInfoScreen = new Button("UsersInfo");
        Button showManageUsersScreen = new Button("Delete User");
        Button showManageDocumentsScreen = new Button("Delete Document");

        // Sự kiện khi nhấn các nút
        addDocumentButton.setOnAction(e -> showAddDocumentScreen());
        viewDocumentsButton.setOnAction(e -> showViewDocumentsScreen());
        addUserButton.setOnAction(e -> showAddUserScreen());
        borrowDocumentButton.setOnAction(e -> showBorrowDocumentScreen());
        showUsersInfoScreen.setOnAction(e -> showUsersInfoScreen());
        showManageDocumentsScreen.setOnAction(e -> showManageDocumentsScreen());
        showManageUsersScreen.setOnAction(e -> showManageUsersScreen());

        VBox layout = new VBox(10, welcomeLabel, addDocumentButton, viewDocumentsButton, addUserButton, borrowDocumentButton, showUsersInfoScreen, showManageUsersScreen, showManageDocumentsScreen);
        layout.setSpacing(10);

        // Kích thước lớn hơn (800x600)
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);

        // Cho phép thay đổi kích thước và đặt ở giữa màn hình
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private static int currentId = 1; // Biến tĩnh dùng chung

    private void showAddDocumentScreen() {
        Label addDocumentLabel = new Label("Add a new Document");

        // Tạo các TextField để nhập thông tin tài liệu
        TextField titleField = new TextField();
        titleField.setPromptText("Enter document title");

        TextField authorField = new TextField();
        authorField.setPromptText("Enter author");

        TextField copiesField = new TextField();
        copiesField.setPromptText("Enter number of copies");

        TextField genreField = new TextField();
        genreField.setPromptText("Enter genre");

        Button addButton = new Button("Add Document");
        Button backButton = new Button("Back to Main Menu");

        // Sự kiện khi nhấn nút "Add Document"
        addButton.setOnAction(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String genre = genreField.getText();

            // Kiểm tra xem số lượng bản sao có phải là một số nguyên hợp lệ hay không
            int copiesAvailable = 0;
            try {
                copiesAvailable = Integer.parseInt(copiesField.getText());
            } catch (NumberFormatException ex) {
                // Hiển thị thông báo lỗi nếu không phải là số nguyên
                Label errorLabel = new Label("Please enter a valid number for copies.");
                VBox errorLayout = new VBox(10, errorLabel);
                Scene errorScene = new Scene(errorLayout, 400, 200);
                Stage errorStage = new Stage();
                errorStage.setScene(errorScene);
                errorStage.setTitle("Input Error");
                errorStage.show();
                return; // Ngừng xử lý nếu có lỗi
            }

            // Tạo tài liệu mới với ID tăng dần
            String newId = String.valueOf(currentId++);
            documentList.add(new Book(newId, title, author, copiesAvailable, genre));

            // Lưu danh sách tài liệu vào file
            saveDocumentsToFile();

            // Quay lại menu chính
            start(primaryStage);
        });

        backButton.setOnAction(e -> start(primaryStage));

        VBox layout = new VBox(10, addDocumentLabel, titleField, authorField, copiesField, genreField, addButton, backButton);
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
    }


    private void showViewDocumentsScreen() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");

        // Tiêu đề cho danh sách sách
        Label viewDocumentsLabel = new Label("Danh sách thư viện");
        viewDocumentsLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-alignment: center;");

        // Tạo TableView để hiển thị thông tin sách
        TableView<Book> tableView = new TableView<>();

        // Cột ID
        TableColumn<Book, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Cột Title
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        // Cột Author
        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        // Cột Genre
        TableColumn<Book, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // Cột Copies Available
        TableColumn<Book, Integer> copiesAvailableColumn = new TableColumn<>("Copies Available");
        copiesAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("copiesAvailable"));

        // Thêm các cột vào bảng
        tableView.getColumns().addAll(idColumn, titleColumn, authorColumn, genreColumn, copiesAvailableColumn);

        // Thêm danh sách sách vào TableView
        tableView.setItems(documentList);  // documentList là ObservableList<Book>

        // Thêm TableView vào layout
        layout.getChildren().addAll(viewDocumentsLabel, tableView);

        // Nút quay lại menu chính
        Button backButton = new Button("Quay lại Menu Chính");
        backButton.setOnAction(e -> start(primaryStage));
        layout.getChildren().add(backButton);

        // Hiển thị giao diện mới
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
    }

    // Phương thức để lưu danh sách tài liệu vào file JSON
    private void saveDocumentsToFile() {
        JSONArray documentsArray = new JSONArray();

        for (Document document : documentList) {
            JSONObject documentObject = new JSONObject();
            documentObject.put("id", document.getId());
            documentObject.put("title", document.getTitle());
            documentObject.put("author", document.getAuthor());
            documentObject.put("copiesAvailable", document.getCopiesAvailable());

            // Kiểm tra nếu tài liệu là sách (Book), thì thêm genre
            if (document instanceof Book) {
                Book book = (Book) document;
                documentObject.put("genre", book.getGenre());
            }

            documentsArray.put(documentObject);
        }

        // Lưu currentId và tài liệu vào file
        JSONObject rootObject = new JSONObject();
        rootObject.put("currentId", currentId); // Lưu lại currentId
        rootObject.put("documents", documentsArray);

        try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(rootObject.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDocumentsFromFile() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            StringBuilder content = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                content.append((char) ch);
            }

            // Kiểm tra xem file có dữ liệu hay không
            if (content.length() > 0) {
                try {
                    JSONObject rootObject = new JSONObject(content.toString());

                    // Tải currentId từ file
                    currentId = rootObject.optInt("currentId", 1); // Nếu không có, mặc định là 1

                    // Lấy mảng tài liệu
                    JSONArray documentsArray = rootObject.optJSONArray("documents");
                    if (documentsArray != null) {
                        // Làm sạch danh sách trước khi thêm tài liệu mới
                        documentList.clear();

                        // Thêm tài liệu mới từ file vào danh sách
                        for (int i = 0; i < documentsArray.length(); i++) {
                            JSONObject documentObject = documentsArray.getJSONObject(i);

                            // Kiểm tra nếu tài liệu có trường "id" và các trường bắt buộc khác
                            if (documentObject.has("id") && documentObject.has("title") && documentObject.has("author") && documentObject.has("copiesAvailable")) {
                                String id = documentObject.getString("id");
                                String title = documentObject.getString("title");
                                String author = documentObject.getString("author");
                                int copiesAvailable = documentObject.getInt("copiesAvailable");

                                String genre = documentObject.optString("genre", "");

                                // Thêm tài liệu vào danh sách
                                documentList.add(new Book(id, title, author, copiesAvailable, genre));
                            }
                        }
                    }
                } catch (org.json.JSONException e) {
                    System.out.println("Error parsing JSON data: " + e.getMessage());
                    // Xử lý khi không thể phân tích được file JSON
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showManageDocumentsScreen() {
        loadDocumentsFromFile(); // Đảm bảo danh sách tài liệu được tải từ file

        // Tạo bảng hiển thị danh sách tài liệu
        TableView<Document> documentTable = new TableView<>();

        // Các cột hiển thị thông tin tài liệu
        TableColumn<Document, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Document, String> titleColumn = new TableColumn<>("Tên Tài Liệu");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Document, String> authorColumn = new TableColumn<>("Tác Giả");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        // Thêm các cột vào bảng
        documentTable.getColumns().addAll(idColumn, titleColumn, authorColumn);

        // Thêm dữ liệu vào bảng
        ObservableList<Document> documentData = FXCollections.observableArrayList(documentList);
        documentTable.setItems(documentData);

        // Nút Xóa
        Button deleteButton = new Button("Xóa Tài Liệu");
        deleteButton.setOnAction(e -> {
            Document selectedDocument = documentTable.getSelectionModel().getSelectedItem();
            if (selectedDocument != null) {
                documentList.remove(selectedDocument); // Xóa khỏi danh sách
                saveDocumentsToFile(); // Lưu danh sách mới vào file
                documentData.remove(selectedDocument); // Cập nhật bảng
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Không có tài liệu được chọn");
                alert.setContentText("Vui lòng chọn tài liệu để xóa.");
                alert.showAndWait();
            }
        });

        // Nút Quay lại
        Button backButton = new Button("Quay lại Menu Chính");
        backButton.setOnAction(e -> start(primaryStage));

        // Bố trí giao diện
        VBox layout = new VBox(10, documentTable, deleteButton, backButton);
        layout.setStyle("-fx-padding: 20px;");
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
    }

    private void showAddUserScreen() {
        // Tiêu đề màn hình
        Label addUserLabel = new Label("Add a new User");

        // Các trường nhập liệu cho người dùng
        TextField userIdField = new TextField();
        userIdField.setPromptText("Enter user ID");

        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter user name");

        ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("Member", "Employee");
        userTypeComboBox.setPromptText("Select user type");

        Button addButton = new Button("Add User");
        Button backButton = new Button("Back to Main Menu");

        // Sự kiện khi nhấn nút "Add User"
        addButton.setOnAction(e -> {
            String userId = userIdField.getText();
            String userName = userNameField.getText();
            String userType = userTypeComboBox.getValue();

            // Kiểm tra dữ liệu nhập vào
            if (userId.isEmpty() || userName.isEmpty() || userType == null) {
                showError("Please fill in all fields.");
                return;
            }

            // Kiểm tra ID có trùng lặp không (giả sử bạn có một danh sách người dùng)
            boolean idExists = false;
            for (User user : userList) {
                if (user.getId().equals(userId)) {
                    idExists = true;
                    break;
                }
            }

            if (idExists) {
                showError("User ID already exists.");
                return;
            }

            // Tạo người dùng mới và thêm vào danh sách
            User newUser = null;
            if (userType.equals("Member")) {
                newUser = new Member(userId, userName);
            } else if (userType.equals("Employee")) {
                newUser = new Employee(userId, userName);
            }

            if (newUser != null) {
                userList.add(newUser);
                saveUsersToFile();  // Lưu danh sách người dùng vào file
            }

            // Quay lại menu chính
            start(primaryStage);
        });

        // Sự kiện khi nhấn nút "Back"
        backButton.setOnAction(e -> start(primaryStage));

        // Bố cục giao diện
        VBox layout = new VBox(10, addUserLabel, userIdField, userNameField, userTypeComboBox, addButton, backButton);
        layout.setSpacing(10);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
    }

    // Phương thức hiển thị thông báo lỗi
    private void showError(String message) {
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red;");
        VBox errorLayout = new VBox(10, errorLabel);
        Scene errorScene = new Scene(errorLayout, 400, 200);
        Stage errorStage = new Stage();
        errorStage.setScene(errorScene);
        errorStage.setTitle("Input Error");
        errorStage.show();
    }

    // Phương thức để lưu danh sách người dùng vào file
    private void saveUsersToFile() {
        JSONArray usersArray = new JSONArray();

        for (User user : userList) {
            JSONObject userObject = new JSONObject();
            userObject.put("id", user.getId());
            userObject.put("name", user.getName());

            // Kiểm tra loại người dùng
            if (user instanceof Member) {
                userObject.put("type", "Member");
            } else if (user instanceof Employee) {
                userObject.put("type", "Employee");
            }

            usersArray.put(userObject);
        }

        try (FileWriter file = new FileWriter("users.json")) {
            file.write(usersArray.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức để tải danh sách người dùng từ file
    private void loadUsersFromFile() {
        try (FileReader reader = new FileReader("users.json")) {
            StringBuilder content = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                content.append((char) ch);
            }

            if (content.length() > 0) {
                JSONArray usersArray = new JSONArray(content.toString());
                userList.clear();  // Xóa hết danh sách cũ

                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject userObject = usersArray.getJSONObject(i);
                    String id = userObject.getString("id");
                    String name = userObject.getString("name");
                    String type = userObject.getString("type");

                    User user = null;
                    if (type.equals("Member")) {
                        user = new Member(id, name);
                    } else if (type.equals("Employee")) {
                        user = new Employee(id, name);
                    }

                    if (user != null) {
                        userList.add(user);
                    }
                }
            }
        } catch (IOException | org.json.JSONException e) {
            e.printStackTrace();
        }
    }

    private void showManageUsersScreen() {
        loadUsersFromFile(); // Đảm bảo danh sách người dùng được tải từ file

        // Tạo bảng hiển thị danh sách người dùng
        TableView<User> userTable = new TableView<>();

        // Các cột hiển thị thông tin người dùng
        TableColumn<User, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Tên");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Thêm các cột vào bảng
        userTable.getColumns().addAll(idColumn, nameColumn);

        // Thêm dữ liệu vào bảng
        ObservableList<User> userData = FXCollections.observableArrayList(userList);
        userTable.setItems(userData);

        // Nút Xóa
        Button deleteButton = new Button("Xóa Người Dùng");
        deleteButton.setOnAction(e -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                userList.remove(selectedUser); // Xóa khỏi danh sách
                saveUsersToFile(); // Lưu danh sách mới vào file
                userData.remove(selectedUser); // Cập nhật bảng
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Không có người dùng được chọn");
                alert.setContentText("Vui lòng chọn người dùng để xóa.");
                alert.showAndWait();
            }
        });

        // Nút Quay lại
        Button backButton = new Button("Quay lại Menu Chính");
        backButton.setOnAction(e -> start(primaryStage));

        // Bố trí giao diện
        VBox layout = new VBox(10, userTable, deleteButton, backButton);
        layout.setStyle("-fx-padding: 20px;");
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
    }

    private void showBorrowDocumentScreen() {
            loadDocumentsFromFile();  // Đảm bảo dữ liệu luôn được cập nhật từ file
            loadUsersFromFile();  // Tải danh sách người dùng

            VBox layout = new VBox(10);
            layout.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");

            Label borrowDocumentLabel = new Label("Mượn và trả Tài Liệu");
            borrowDocumentLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-alignment: center;");

            // Tạo ComboBox cho tên người mượn (đã được lưu trong danh sách người dùng)
            ComboBox<String> memberComboBox = new ComboBox<>();
            for (User user : userList) {  // userList là danh sách người dùng
                memberComboBox.getItems().add(user.getName());  // Thêm tên người dùng vào ComboBox
            }
            memberComboBox.setPromptText("Chọn tên người mượn");

            // Tạo ComboBox cho tên sách
            ComboBox<String> bookComboBox = new ComboBox<>();
            for (Document document : documentList) {
                if (document instanceof Book) {
                    bookComboBox.getItems().add(((Book) document).getTitle());  // Chỉ lấy tên sách từ danh sách tài liệu
                }
            }
            bookComboBox.setPromptText("Chọn tên sách");

            // Tạo DatePicker cho ngày mượn
            DatePicker borrowDatePicker = new DatePicker();
            borrowDatePicker.setPromptText("Chọn ngày mượn");

            // Tạo DatePicker cho ngày trả
            DatePicker returnDatePicker = new DatePicker();
            returnDatePicker.setPromptText("Chọn ngày trả");

            // Tạo thông báo và nút mượn tài liệu
            Label messageLabel = new Label();
            Button borrowButton = new Button("Mượn Tài Liệu");
            borrowButton.setOnAction(e -> {
                String selectedMemberName = memberComboBox.getValue();
                String selectedBookTitle = bookComboBox.getValue();
                String borrowDate = borrowDatePicker.getValue() != null ? borrowDatePicker.getValue().toString() : null;
                String returnDate = returnDatePicker.getValue() != null ? returnDatePicker.getValue().toString() : null;

                if (selectedMemberName == null || selectedBookTitle == null || borrowDate == null || returnDate == null) {
                    messageLabel.setText("Vui lòng điền đầy đủ thông tin.");
                } else {
                    // Tìm người mượn trong danh sách người dùng
                    User selectedMember = null;
                    for (User user : userList) {
                        if (user.getName().equals(selectedMemberName)) {
                            selectedMember = user;
                            break;
                        }
                    }

                    // Tìm tài liệu trong danh sách tài liệu
                    Book selectedBook = null;
                    for (Document document : documentList) {
                        if (document instanceof Book && ((Book) document).getTitle().equals(selectedBookTitle)) {
                            selectedBook = (Book) document;
                            break;
                        }
                    }

                    if (selectedBook != null && selectedMember != null) {
                        // Kiểm tra số lượng bản sao
                        if (selectedBook.getCopiesAvailable() > 0) {
                            // Mượn tài liệu
                            selectedBook.borrow();
                            saveDocumentsToFile();  // Cập nhật lại file tài liệu
                            messageLabel.setText("Tài liệu mượn thành công! Ngày mượn: " + borrowDate + ", Ngày trả: " + returnDate);
                        } else {
                            messageLabel.setText("Tài liệu không còn bản sao để mượn.");
                        }
                    }
                }
            });

            Button backButton = new Button("Quay lại Menu Chính");
            backButton.setOnAction(e -> start(primaryStage));

            // Thêm các thành phần vào layout
            layout.getChildren().addAll(
                    borrowDocumentLabel,
                    memberComboBox,
                    bookComboBox,
                    borrowDatePicker,
                    returnDatePicker,
                    borrowButton,
                    messageLabel,
                    backButton
            );

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
    }

    private void showUsersInfoScreen() {
        loadDocumentsFromFile();  // Đảm bảo dữ liệu luôn được cập nhật từ file
        loadUsersFromFile();  // Tải danh sách người dùng

        // Tạo bảng (TableView) để hiển thị thông tin người mượn
        TableView<BorrowedInfo> table = new TableView<>();

        // Các cột trong bảng
        TableColumn<BorrowedInfo, String> nameColumn = new TableColumn<>("Tên Người Mượn");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<BorrowedInfo, String> bookTitleColumn = new TableColumn<>("Tên Sách");
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        TableColumn<BorrowedInfo, String> borrowDateColumn = new TableColumn<>("Ngày Mượn");
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));

        TableColumn<BorrowedInfo, String> returnDateColumn = new TableColumn<>("Ngày Trả");
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        // Thêm các cột vào bảng
        table.getColumns().addAll(nameColumn, bookTitleColumn, borrowDateColumn, returnDateColumn);

        // Tạo danh sách để lưu thông tin mượn của từng người
        ObservableList<BorrowedInfo> data = FXCollections.observableArrayList();

        // Lấy thông tin người mượn từ danh sách người dùng và tài liệu
        for (User user : userList) {
            for (Document document : documentList) {
                if (document instanceof Book) {
                    Book book = (Book) document;

                    // Lấy thông tin mượn từ tài liệu và người mượn (giả sử đã có thông tin mượn)
                    // Bạn có thể thay đổi cách lưu thông tin mượn này tùy theo cách bạn tổ chức dữ liệu
                    if (user instanceof Member) {
                        Member member = (Member) user;
                        // Thêm thông tin mượn vào danh sách data
                        data.add(new BorrowedInfo(member.getName(), book.getTitle(), "01/01/2024", "15/01/2024"));
                    }
                }
            }
        }

        // Thiết lập dữ liệu cho bảng
        table.setItems(data);

        // Layout
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");

        Label usersLabel = new Label("Danh Sách Người Mượn");
        usersLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-alignment: center;");

        Button backButton = new Button("Quay lại Menu Chính");
        backButton.setOnAction(e -> start(primaryStage));

        // Thêm các thành phần vào layout
        layout.getChildren().addAll(usersLabel, table, backButton);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
    }

}