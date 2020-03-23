INSERT INTO `equifax`.`Roles` (`id_role`, `name`) VALUES ('1', 'ROLE_ADMIN');
INSERT INTO `equifax`.`Roles` (`id_role`, `name`) VALUES ('2', 'ROLE_DIRECTOR');
INSERT INTO `equifax`.`Roles` (`id_role`, `name`) VALUES ('3', 'ROLE_TEACHER');

--.withUser(user.username("god").password("asdgod").roles("ADMIN", "DIRECTOR", "TEACHER"))
--.withUser(user.username("admin").password("asdadmin").roles("ADMIN"))
--.withUser(user.username("director1").password("asddir").roles("DIRECTOR"))
--.withUser(user.username("teacher1").password("asdtea").roles("TEACHER"));
