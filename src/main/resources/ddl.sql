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
