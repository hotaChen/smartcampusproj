const API_BASE_URL = 'http://localhost:8081';

// API endpoints
const API_ENDPOINTS = {
  // Authentication
  LOGIN: `${API_BASE_URL}/api/auth/login`,
  USER_INFO: `${API_BASE_URL}/api/auth/user-info`,
  CHANGE_PASSWORD: `${API_BASE_URL}/api/auth/change-password`,
  RESET_PASSWORD: `${API_BASE_URL}/api/auth/reset-password`,
  REGISTER: `${API_BASE_URL}/api/auth/register`,
  
  // Appointments
  APPOINTMENTS: `${API_BASE_URL}/api/appointments`,
  MY_APPOINTMENTS: `${API_BASE_URL}/api/appointments/mine`,
  CANCEL_APPOINTMENT: (id) => `${API_BASE_URL}/api/appointments/${id}/cancel`,
  APPROVE_APPOINTMENT: (id) => `${API_BASE_URL}/api/appointments/${id}/approve`,
  COMPLETE_APPOINTMENT: (id) => `${API_BASE_URL}/api/appointments/${id}/complete`,
  
  // Classrooms
  CLASSROOMS: `${API_BASE_URL}/api/classrooms`,
  CLASSROOM: (id) => `${API_BASE_URL}/api/classrooms/${id}`,
  
  // Courses
  COURSES: `${API_BASE_URL}/api/courses`,
  COURSE: (id) => `${API_BASE_URL}/api/courses/${id}`,
  
  // Course Selections
  MY_COURSE_SELECTIONS: `${API_BASE_URL}/api/course-selections/my`,
  COURSE_SELECTION: (id) => `${API_BASE_URL}/api/course-selections/${id}`,
  
  // Syllabus
  SYLLABUS: (courseId) => `${API_BASE_URL}/api/syllabus/${courseId}`,
  
  // Timetables
  TIMETABLES_BY_COURSE: (courseId) => `${API_BASE_URL}/api/timetables/course/${courseId}`,
  TIMETABLES: `${API_BASE_URL}/api/timetables`,
  TIMETABLE: (id) => `${API_BASE_URL}/api/timetables/${id}`,
  
  // Student Timetable
  STUDENT_TIMETABLE: `${API_BASE_URL}/api/student/timetable/me`,
  
  // Teacher Timetable
  TEACHER_TIMETABLE: `${API_BASE_URL}/api/teacher/timetable/me`
};

// Utility function to make authenticated requests
function apiRequest(endpoint, options = {}) {
  const token = localStorage.getItem('token');
  const defaultOptions = {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers
    }
  };
  
  if (token && !options.headers?.['Authorization']) {
    defaultOptions.headers['Authorization'] = `Bearer ${token}`;
  }
  
  return fetch(endpoint, {
    ...options,
    ...defaultOptions
  });
}