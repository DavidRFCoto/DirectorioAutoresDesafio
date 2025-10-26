 CREATE DATABASE Authors;
  USE Authors;
 CREATE TABLE IF NOT EXISTS literarygenre (
    genId INT AUTO_INCREMENT PRIMARY KEY,          
    nombreGenero VARCHAR(100) NOT NULL UNIQUE,     
    descripcionGenero TEXT                         
);
CREATE TABLE IF NOT EXISTS author (
    autId INT AUTO_INCREMENT PRIMARY KEY,          -- ID unico para cada autor
    nombreAutor VARCHAR(100) NOT NULL,             -- Nombre del autor
    apellidoAutor VARCHAR(100) NOT NULL,           -- Apellido del autor
    email VARCHAR(100),                            -- Email del autor
    fechaNacimiento DATE,                          -- Fecha de nacimiento del autor
    telefono VARCHAR(15),                          -- Telefono del autor
    generoId INT,                                  -- Llave foranea para el genero literario
    
    UNIQUE (nombreAutor, apellidoAutor),
    
    FOREIGN KEY (generoId) REFERENCES literarygenre(genId) ON DELETE SET NULL
);
INSERT INTO literarygenre (nombreGenero, descripcionGenero) VALUES
 ('Novela Histórica', 'Narrativa que reconstruye eventos del pasado con personajes ficticios.'),
 ('Poesía Lírica', 'Composiciones que expresan sentimientos y emociones profundas.'),
 ('Ensayo Filosófico', 'Textos de reflexión sobre cuestiones fundamentales de la existencia.'),
 ('Realismo Mágico', 'Historias que mezclan elementos fantásticos con la realidad cotidiana.'),
 ('Thriller Psicológico', 'Relatos de suspenso centrados en la mente de los personajes.');
SELECT * FROM literarygenre;
DESCRIBE author;

select *from author;
