CREATE TABLE allocations (
	id              BIGINT auto_increment NOT NULL,
	room_id         BIGINT NOT NULL,
	employee_name   VARCHAR(20) NOT NULL,
	employee_email  VARCHAR(30) NOT NULL,
	subject         VARCHAR(60) NOT NULL,
	start_at        DATETIME(3) NOT NULL,
	end_at          DATETIME(3) NOT NULL,
	created_at      DATETIME(3) NOT NULL,
	updated_at      DATETIME(3) NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT `allocations_rooms` FOREIGN KEY (room_id) REFERENCES rooms (id)
);