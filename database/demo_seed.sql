USE student_db;
-- Optional demonstration records used by the screenshots. Run once after student_db.sql.
-- The six sample files are included under php/uploads/.
INSERT INTO students (name, roll_number, email, course, created_at) VALUES
('Aanya Sharma', '26CSE104', 'aanya.sharma@college.edu', 'Computer Science and Engineering', '2026-11-02 09:00:00'),
('Rohan Menon', '26IT028', 'rohan.menon@college.edu', 'Information Technology', '2026-11-01 09:00:00'),
('Meera Nair', '26AID056', 'meera.nair@college.edu', 'AI and Data Science', '2026-10-30 09:00:00');

INSERT INTO student_files (student_id, original_name, stored_name, file_path, mime_type, file_size, file_category, uploaded_at)
SELECT s.id, f.original_name, f.stored_name, f.file_path, f.mime_type, f.file_size, f.file_category, f.uploaded_at
FROM students s
JOIN (
 SELECT '26CSE104' roll_number, 'Student-ID.pdf' original_name, 'demo-aanya-id.pdf' stored_name, 'uploads/documents/demo-aanya-id.pdf' file_path, 'application/pdf' mime_type, 1909 file_size, 'document' file_category, '2026-11-02 10:00:00' uploaded_at
 UNION ALL SELECT '26CSE104','Profile-photo.jpg','demo-aanya-profile.jpg','uploads/profiles/demo-aanya-profile.jpg','image/jpeg',9065,'profile','2026-11-02 10:05:00'
 UNION ALL SELECT '26IT028','Identity-proof.pdf','demo-rohan-id.pdf','uploads/documents/demo-rohan-id.pdf','application/pdf',1931,'document','2026-11-01 10:00:00'
 UNION ALL SELECT '26AID056','Certificate.pdf','demo-meera-certificate.pdf','uploads/documents/demo-meera-certificate.pdf','application/pdf',1912,'document','2026-10-30 10:00:00'
 UNION ALL SELECT '26AID056','Profile.png','demo-meera-profile.png','uploads/profiles/demo-meera-profile.png','image/png',5100,'profile','2026-10-30 10:05:00'
 UNION ALL SELECT '26AID056','Workshop-Record.pdf','demo-meera-workshop.pdf','uploads/documents/demo-meera-workshop.pdf','application/pdf',1919,'document','2026-10-30 10:10:00'
) f ON f.roll_number = s.roll_number;

-- Create a portal/admin account separately using the password_hash instructions in README.md.
