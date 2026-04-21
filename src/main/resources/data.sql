/* data.sql */
-- Wait to insert if table exists, IGNORE handles unique constraint issues on reload
INSERT IGNORE INTO users (id, name, email, password, role) VALUES 
(1, 'System Admin', 'admin@system.com', '$2a$10$xV.J/.u57sD0J5wz9iN/Y.n87B576lZ0m4sOqK4a43q1KxVlO0A5O', 'ADMIN'), -- password: password
(2, 'City Admin', 'cityadmin@system.com', '$2a$10$xV.J/.u57sD0J5wz9iN/Y.n87B576lZ0m4sOqK4a43q1KxVlO0A5O', 'ADMIN'),
(3, 'John Worker', 'john@staff.com', '$2a$10$xV.J/.u57sD0J5wz9iN/Y.n87B576lZ0m4sOqK4a43q1KxVlO0A5O', 'STAFF'),
(4, 'Sarah Palmer', 'sarah@staff.com', '$2a$10$xV.J/.u57sD0J5wz9iN/Y.n87B576lZ0m4sOqK4a43q1KxVlO0A5O', 'STAFF'),
(5, 'Mike Engineer', 'mike@staff.com', '$2a$10$xV.J/.u57sD0J5wz9iN/Y.n87B576lZ0m4sOqK4a43q1KxVlO0A5O', 'STAFF'),
(6, 'Alice Citizen', 'alice@gmail.com', '$2a$10$xV.J/.u57sD0J5wz9iN/Y.n87B576lZ0m4sOqK4a43q1KxVlO0A5O', 'USER'),
(7, 'Bob Citizen', 'bob@gmail.com', '$2a$10$xV.J/.u57sD0J5wz9iN/Y.n87B576lZ0m4sOqK4a43q1KxVlO0A5O', 'USER'),
(8, 'Charlie Citizen', 'charlie@gmail.com', '$2a$10$xV.J/.u57sD0J5wz9iN/Y.n87B576lZ0m4sOqK4a43q1KxVlO0A5O', 'USER'),
(9, 'Dave Citizen', 'dave@gmail.com', '$2a$10$xV.J/.u57sD0J5wz9iN/Y.n87B576lZ0m4sOqK4a43q1KxVlO0A5O', 'USER'),
(10, 'Eve Citizen', 'eve@gmail.com', '$2a$10$xV.J/.u57sD0J5wz9iN/Y.n87B576lZ0m4sOqK4a43q1KxVlO0A5O', 'USER');

INSERT IGNORE INTO categories (id, name) VALUES 
(1, 'Garbage'),
(2, 'Water'),
(3, 'Road'),
(4, 'Electricity'),
(5, 'Sewage'),
(6, 'Parks');

INSERT IGNORE INTO complaints (id, title, description, status, user_id, category_id, created_at) VALUES 
(1, 'Pothole on Main St', 'There is a huge pothole causing traffic near the intersection.', 'PENDING', 6, 3, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(2, 'Garbage piled up', 'Garbage bin is overflowing and hasn’t been collected for 3 days.', 'PENDING', 6, 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 'Water pipe leak', 'Fresh water is leaking from the main pipe.', 'IN_PROGRESS', 7, 2, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(4, 'Street light broken', 'The street light is broken on 5th Avenue.', 'RESOLVED', 8, 4, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(5, 'Sewage smell', 'Terrible smell coming from the drain.', 'REJECTED', 9, 5, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(6, 'Tree fallen in Park', 'A big tree has fallen over the walkway.', 'PENDING', 10, 6, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(7, 'Another Pothole', 'Pothole on Baker st.', 'IN_PROGRESS', 6, 3, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(8, 'Power outage', 'No electricity in sector 4.', 'RESOLVED', 7, 4, DATE_SUB(NOW(), INTERVAL 15 DAY)),
(9, 'Water supply muddy', 'Brown water coming from taps.', 'PENDING', 8, 2, NOW()),
(10, 'Garbage dump near school', 'Illegal dumping of garbage near local school.', 'IN_PROGRESS', 9, 1, DATE_SUB(NOW(), INTERVAL 1 DAY));

INSERT IGNORE INTO assignments (id, complaint_id, staff_id) VALUES 
(1, 3, 3),
(2, 7, 4),
(3, 10, 5),
(4, 4, 3),
(5, 8, 4);

INSERT IGNORE INTO complaint_logs (complaint_id, old_status, new_status, changed_by, changed_at) VALUES
(3, 'PENDING', 'IN_PROGRESS', 1, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(4, 'PENDING', 'IN_PROGRESS', 2, DATE_SUB(NOW(), INTERVAL 8 DAY)),
(4, 'IN_PROGRESS', 'RESOLVED', 3, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(7, 'PENDING', 'IN_PROGRESS', 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(8, 'PENDING', 'RESOLVED', 2, DATE_SUB(NOW(), INTERVAL 14 DAY)),
(10, 'PENDING', 'IN_PROGRESS', 1, DATE_SUB(NOW(), INTERVAL 12 HOUR));
