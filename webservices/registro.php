<?php
    $con = mysqli_connect("localhost","root","","qrmascota");

    $nombre =$_POST["nombre"];
    $apellido =$_POST["apellido"];
    $email = $_POST["email"];
    $celular =$_POST["celular"];
    $contrasena =$_POST["contrasena"]

    $statement = mysqli_prepare($con, "INSERT INTO user (nombre,apellido,email,celular,contrasena) VALUES (?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "siss", $nombre, $apellido, $email, $celular,$contrasena);
    mysqli_stmt_execute($statement);

    $response = array();
    $responde ["success"] = true;

    echo json_encode($response);


?>