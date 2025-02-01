# Generated by Django 5.0.1 on 2025-01-26 18:27

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('tareas', '0004_alter_user_rol'),
    ]

    operations = [
        migrations.AlterField(
            model_name='tasks',
            name='task_status',
            field=models.IntegerField(choices=[(0, 'PENDIENTE'), (1, 'COMPLETADA'), (2, 'CANCELADA'), (3, 'ENPROGRESO')], default=0),
        ),
    ]
