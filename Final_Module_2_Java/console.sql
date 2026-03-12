DROP SCHEMA IF EXISTS course_management CASCADE;
CREATE SCHEMA IF NOT EXISTS course_management;
SET search_path TO course_management;

CREATE TABLE admin (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);
CREATE TABLE student (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         dob DATE NOT NULL,
                         email VARCHAR(100) NOT NULL UNIQUE,
                         sex BOOLEAN NOT NULL, -- TRUE: Nam, FALSE: Nữ (hoặc tùy bạn quy ước)
                         phone VARCHAR(20),
                         password VARCHAR(255) NOT NULL,
                         created_at DATE DEFAULT CURRENT_DATE
);
CREATE TABLE course (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        duration INT NOT NULL, -- Thời lượng (giờ)
                        instructor VARCHAR(100) NOT NULL,
                        created_at DATE DEFAULT CURRENT_DATE
);
CREATE TABLE enrollment (
                            id SERIAL PRIMARY KEY,
                            student_id INT NOT NULL,
                            course_id INT NOT NULL,
                            registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            status VARCHAR(20) DEFAULT 'WAITING',

    -- Ràng buộc khóa ngoại
                            CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
                            CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,

    -- Giới hạn các giá trị trạng thái (WAITING, DENIED, CANCEL, CONFIRMED...)
                            CONSTRAINT chk_status CHECK (status IN ('WAITING', 'DENIED', 'CANCEL', 'CONFIRMED'))
);


