CREATE TABLE `equifax`.`Courses` (
  `id_course` INT NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `code` VARCHAR(4) NOT NULL COMMENT 'Course code',
  `name` VARCHAR(64) NOT NULL COMMENT 'Course name',
  PRIMARY KEY (`id_course`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC))
COMMENT = 'College courses';

CREATE TABLE `equifax`.`Students` (
  `id_student` INT NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `course_id` INT NOT NULL COMMENT 'FK to Courses. The course to which the student belongs',
  `rut` VARCHAR(11) NOT NULL COMMENT 'Student\'s rut',
  `name` VARCHAR(32) NOT NULL COMMENT 'Student\'s name',
  `last_name` VARCHAR(32) NOT NULL COMMENT 'Student\'s last name',
  `age` TINYINT NOT NULL COMMENT 'Student\'s age',
  PRIMARY KEY (`id_student`),
  UNIQUE INDEX `rut_UNIQUE` (`rut` ASC),
  INDEX `IFK_Students_Courses` (`course_id` ASC),
  CONSTRAINT `FK_Students_Courses`
    FOREIGN KEY (`course_id`)
    REFERENCES `equifax`.`Courses` (`id_course`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
COMMENT = 'College students';

CREATE TABLE `equifax`.`Users` (
  `id_user` INT NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `username` VARCHAR(32) NOT NULL COMMENT 'Login username',
  `password` VARCHAR(255) NOT NULL COMMENT 'Login encrypted password',
  `enabled` TINYINT NOT NULL DEFAULT 1 COMMENT 'Whether the user is enabled or not',
  PRIMARY KEY (`id_user`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
COMMENT = 'Application users';

CREATE TABLE `equifax`.`Roles` (
  `id_role` INT NOT NULL COMMENT 'PK',
  `name` VARCHAR(16) NOT NULL COMMENT 'Role name',
  PRIMARY KEY (`id_role`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
COMMENT = 'Application roles';

CREATE TABLE `equifax`.`Users_Roles` (
  `user_id` INT NOT NULL COMMENT 'PFK to Users',
  `role_id` INT NOT NULL COMMENT 'PFK to Roles',
  PRIMARY KEY (`user_id`, `role_id`),
  INDEX `FK_Users_Roles_Roles_idx` (`role_id` ASC),
  CONSTRAINT `FK_Users_Roles_Users`
    FOREIGN KEY (`user_id`)
    REFERENCES `equifax`.`Users` (`id_user`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_Users_Roles_Roles`
    FOREIGN KEY (`role_id`)
    REFERENCES `equifax`.`Roles` (`id_role`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
COMMENT = 'Users and Roles join table';
