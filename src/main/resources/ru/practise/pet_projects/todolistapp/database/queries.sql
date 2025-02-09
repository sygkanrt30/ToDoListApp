-- Select user info by login
USER_INFO_SELECT_BY_LOGIN = SELECT * FROM users WHERE login = (?);

-- Select user info by username
USER_INFO_SELECT_BY_USERNAME = SELECT * FROM users WHERE username = (?);

-- Select tasks info by username
TASKS_INFO_SELECT_BY_USERNAME = SELECT contents, priority, dedlines, status FROM tasks_content WHERE username = (?);

-- Insert user
INSERT_USER = INSERT INTO users (login, password, username) VALUES (?, ?, ?);

-- Insert task
INSERT_TASK = INSERT INTO tasks_content (username, contents, priority, dedlines, status) VALUES (?, ?, ?, ?, ?);

-- Delete all tasks
DELETE_ALL_TASKS = DELETE FROM tasks_content WHERE username = (?);

-- Delete all completed tasks
DELETE_ALL_COMPLETED_TASKS = DELETE FROM tasks_content WHERE username = (?) AND status = 'выполнено';

-- Execute all tasks
EXECUTE_ALL_TASKS = UPDATE tasks_content SET status = 'выполнено' WHERE username = (?) AND status = 'не выполнено';

-- Rename task
RENAME_TASK = UPDATE tasks_content SET contents = (?) WHERE username = (?) AND contents = (?) AND priority = (?) AND dedlines = (?);

-- Delete task
DELETE_TASK = DELETE FROM tasks_content WHERE username = (?) AND contents = (?) AND priority = (?) AND dedlines = (?);

-- Execute task
EXECUTE_TASK = UPDATE tasks_content SET status = 'выполнено' WHERE username = (?) AND contents = (?) AND priority = (?) AND dedlines = (?);

-- Cancel execution of task
CANSEL_EXECUTION_TASK = UPDATE tasks_content SET status = 'не выполнено' WHERE username = (?) AND contents = (?) AND priority = (?) AND dedlines = (?);