provider "aws" {
    region = "us-east-1"
    access_key = ""
    secret_key = ""
}

resource "aws_default_vpc" "default_vpc" {
    tags = {
        Name = "default vpc"
    }  
}

data "aws_availability_zones" "availability_zones" {}

resource "aws_default_subnet" "default_az1" {
    availability_zone = data.aws_availability_zones.availability_zones.names[0]
    tags = {
        Name = "default subnet"
    }
}

resource "aws_security_group" "ec2_security_group" {
    name = "ec2 security group"
    description = "allow access on ports 8080 and 22"
    vpc_id = aws_default_vpc.default_vpc.id

    #allow access on port 8080
    ingress {
        description = "http proxy access"
        from_port = 8080
        to_port = 8080
        protocol = "tcp"
        cidr_blocks = [ "0.0.0.0/0" ]
    }

    #allow access on port 22
    ingress {
        description = "ssh access"
        from_port = 22
        to_port = 22
        protocol = "tcp"
        cidr_blocks = [ "0.0.0.0/0" ]
    }

    egress {
        from_port = 0
        to_port = 0
        protocol = -1
        cidr_blocks = [ "0.0.0.0/0" ]
    }

    tags = {
        Name = "jenkins server security group"
    }
}

data "aws_ami" "amazon_linux_2" {
    most_recent = true
    owners = [ "amazon" ]

    filter {
        name = "owner-alias"
        values = [ "amazon" ]
    }

    filter {
        name = "name"
        values = [ "amzn2-ami-hvm*" ]
    }
}

resource "aws_instance" "ec2_instance" {
    ami = data.aws_ami.amazon_linux_2.id
    instance_type = "t2.micro"
    subnet_id = aws_default_subnet.default_az1.id
    vpc_security_group_ids = [aws_security_group.ec2_security_group.id]
    key_name = "teste-terraform-jenkins"

    tags = {
        Name = "jenkins server"
    }
}

resource "null_resource" "name" {
    connection {
        type = "ssh"
        user = "ec2-user"
        private_key = file("")
        host = aws_instance.ec2_instance.public_ip
    }

    provisioner "file" {
        source = "install_jenkins.sh"
        destination = "/tmp/install_jenkins.sh"
    }

    provisioner "remote-exec" {
        inline = [ 
            "sudo chmod +x /tmp/install_jenkins.sh",
            "sh /tmp/install_jenkins.sh"
        ]
    }

    depends_on = [ aws_instance.ec2_instance ]
}

output "website_url" {
    value = join("", ["http://", aws_instance.ec2_instance.public_dns, ":", "8080"])
}