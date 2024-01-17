# PEP3 Métodos de Ingeniería de Software
Aplicación web desarrollada como proyecto para la asignatura Métodos de Ingeniería de Software de la USACH.

El foco de este proyecto está en implementar las distintas funcionalidades y cumplir con las reglas de negocio. A diferencia de la PEP 1 y 2, en este proyecto el foco está en el frontend, por lo que se desarrolla una interfaz robusta que cumple con los principios de usabilidad de Nielsen.

Las funcionalidades están dadas por un enunciado que nos sitúa en el contexto de una Universidad que requiere gestionar el proceso de inscripción de asignaturas de los estudiantes, las historias de usuario implementadas fueron las siguientes:
 
El sistema debe permitir el ingreso de los horarios de cada asignatura.
- El sistema debe permitir la inscripción de asignaturas tomando en consideración las
siguientes condiciones:
- Cada semestre lectivo regular los estudiantes de la FING deberán cursar al
menos tres asignaturas de su plan de estudios para permanecer en su carrera.
- El número máximo de asignaturas que puede inscribir el o la estudiante
corresponde al número de asignaturas de su nivel.
- Un estudiante puede inscribir una asignatura si y solo si ha aprobado todos los
prerrequisitos asociados a dicha asignatura.
- Las asignaturas podrán ser cursadas para su aprobación hasta en dos
oportunidades. La reprobación de una asignatura en segunda oportunidad
provoca automáticamente la eliminación del estudiante del programa
académico que cursa. No obstante, las asignaturas semestrales de primer nivel
podrán cursarse hasta en 3 oportunidades.
- No se pueden inscribir asignaturas con tope de horario.
- Cada asignatura tiene un cupo máximo de estudiantes.
- El sistema debe mostrar el número de estudiantes actualmente inscritos en los cursos.
- El sistema debe permitir mostrar una malla interactiva desde donde el alumno puede
inscribir sus asignaturas. Esta malla interactiva debería ser diseñada de tal manera
que ayude a los estudiantes a realizar su proceso de inscripción lo más fácil posible.
Por ejemplo, desde esta malla interactiva se debería poder ver información como:
asignaturas aprobadas, asignaturas inscritas, asignaturas que podría llevar, historial
de notas de una asignatura seleccionada, etc.

## Tecnologias relevantes

- Spring Boot para el desarrollo de dos microservicios que se comunican mediante Feign Client (REST).
- MySQL como base de datos (se crea una por microservicio).
- Next.js para el desarrollo del Frontend.

## Imágenes UI

![alt text](https://github.com/hgallardoaraya/pep3-mingeso/blob/main/imagenes-ui/ingresar-horarios-1.png)

![alt text](https://github.com/hgallardoaraya/pep3-mingeso/blob/main/imagenes-ui/ingresar-horarios-2.png)

![alt text](https://github.com/hgallardoaraya/pep3-mingeso/blob/main/imagenes-ui/ingresar-horarios-3.png)

![alt text](https://github.com/hgallardoaraya/pep3-mingeso/blob/main/imagenes-ui/inscribir-asignatura-1.png)

![alt text](https://github.com/hgallardoaraya/pep3-mingeso/blob/main/imagenes-ui/inscribir-asignatura-2.png)

![alt text](https://github.com/hgallardoaraya/pep3-mingeso/blob/main/imagenes-ui/inscribir-asignatura-3.png)
