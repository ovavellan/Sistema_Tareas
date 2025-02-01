from rest_framework import serializers
from tareas.models import Tasks, Comment, User

class TasksSerializer(serializers.ModelSerializer):
    taskStatus = serializers.SerializerMethodField()
    dueDate = serializers.DateTimeField(source="due_date", allow_null=True)
    studentId = serializers.IntegerField(source="user.id", write_only=True)
    studentName = serializers.CharField(source="user.username", read_only=True)
    task_status = serializers.IntegerField(write_only=True, required=False)  # Hacerlo opcional

    class Meta:
        model = Tasks
        fields = [
            "id",
            "title",
            "description",
            "dueDate",
            "priority",
            "taskStatus",
            "studentId",
            "studentName",
            "task_status",
        ]

    def get_taskStatus(self, obj):
        return obj.get_task_status_display() or "ESTADO NO DEFINIDO"

    def create(self, validated_data):
        user_id = validated_data.pop('user')['id']
        user = User.objects.get(id=user_id)
        task = Tasks.objects.create(user=user, **validated_data)
        return task

    def update(self, instance, validated_data):
        # Actualizar campos simples del modelo
        instance.title = validated_data.get("title", instance.title)
        instance.description = validated_data.get("description", instance.description)
        instance.due_date = validated_data.get("due_date", instance.due_date)
        instance.priority = validated_data.get("priority", instance.priority)

        # Actualizar el estado de la tarea solo si se proporciona un valor explícito
        if 'task_status' in validated_data:
            instance.task_status = validated_data['task_status']

        # Actualizar la relación con el usuario si se proporciona
        if 'user' in validated_data:
            user_id = validated_data['user']['id']
            user = User.objects.get(id=user_id)
            instance.user = user

        instance.save()
        return instance

    taskStatus = serializers.SerializerMethodField()
    dueDate = serializers.DateTimeField(source="due_date", allow_null=True)
    studentId = serializers.IntegerField(source="user.id")
    studentName = serializers.CharField(source="user.username", read_only=True)


    def get_taskStatus(self, obj):
        # Usa el método automático de Django para obtener el texto de STATUS_CHOICES
        return obj.get_task_status_display() or "ESTADO NO DEFINIDO"


class CommentSerializer(serializers.ModelSerializer):
    userName = serializers.CharField(source="user.username", read_only=True)  # Nombre del usuario
    createdAt = serializers.DateTimeField(source="create_at", read_only=True)  # Fecha de creación

    class Meta:
        model = Comment
        fields = ["id", "content", "userName", "createdAt", "task", "user"]

class UserSerializer(serializers.ModelSerializer):
    name = serializers.CharField(source="username")
    password = serializers.SerializerMethodField()
    userRole = (
        serializers.SerializerMethodField()
    )  # Mapear 'rol' a 'ADMINISTRADOR' o 'ESTUDIANTE'

    class Meta:
        model = User
        fields = ["id", "name", "email", "password", "userRole"]

    def get_password(self, obj):
        return None  # Ocultar el valor real de la contraseña

    def get_userRole(self, obj):
        return "ADMINISTRADOR" if obj.rol else "ESTUDIANTE"
