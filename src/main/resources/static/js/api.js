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
  TEACHER_TIMETABLE: `${API_BASE_URL}/api/teacher/timetable/me`,
  
  // Grades
  GRADES_ENTER: `${API_BASE_URL}/api/grades/enter`,
  GRADES_ENTER_BY_STUDENT_NUMBER: `${API_BASE_URL}/api/grades/enter-by-student-number`,
  GRADES_BATCH: `${API_BASE_URL}/api/grades/batch`,
  GRADES_BATCH_BY_STUDENT_NUMBER: `${API_BASE_URL}/api/grades/batch-by-student-number`,
  GRADES_UPDATE: (id) => `${API_BASE_URL}/api/grades/${id}`,
  GRADES_UPDATE_BY_STUDENT_NUMBER: (id) => `${API_BASE_URL}/api/grades/${id}/update-by-student-number`,
  GRADES_DELETE: (id) => `${API_BASE_URL}/api/grades/${id}`,
  GRADES_BY_ID: (id) => `${API_BASE_URL}/api/grades/${id}`,
  GRADES_BY_STUDENT: (id) => `${API_BASE_URL}/api/grades/student/${id}`,
  GRADES_BY_STUDENT_NUMBER: (studentNumber) => `${API_BASE_URL}/api/grades/student/number/${studentNumber}`,
  GRADES_BY_TEACHER: `${API_BASE_URL}/api/grades/teacher`,
  GRADES_STUDENT_SEMESTER: (id, semester) => `${API_BASE_URL}/api/grades/student/${id}/semester/${semester}`,
  GRADES_STUDENT_NUMBER_SEMESTER: (studentNumber, semester) => `${API_BASE_URL}/api/grades/student/number/${studentNumber}/semester/${semester}`,
  GRADES_TEACHER_COURSE_SEMESTER: (courseCode, semester) => `${API_BASE_URL}/api/grades/teacher/course/${courseCode}/semester/${semester}`,
  GRADES_STUDENT_AVERAGE: (id, semester) => `${API_BASE_URL}/api/grades/student/${id}/average/${semester}`,
  GRADES_STUDENT_AVERAGE_BY_STUDENT_NUMBER: (studentNumber, semester) => `${API_BASE_URL}/api/grades/student/number/${studentNumber}/average/${semester}`,
  GRADES_COURSE_AVERAGE: (courseCode, semester) => `${API_BASE_URL}/api/grades/course/${courseCode}/average/${semester}`,
  GRADES_REPORT: (id) => `${API_BASE_URL}/api/grades/report/${id}`,
  GRADES_REPORT_BY_STUDENT_NUMBER: (studentNumber) => `${API_BASE_URL}/api/grades/report/student/number/${studentNumber}`,
  GRADES_MY_REPORT: `${API_BASE_URL}/api/grades/report/my`,  
  // Makeup Exams
  MAKEUP_APPLY: `${API_BASE_URL}/api/makeup-exams/apply`,
  MAKEUP_APPROVE: (id) => `${API_BASE_URL}/api/makeup-exams/${id}/approve`,
  MAKEUP_GRADE: (id) => `${API_BASE_URL}/api/makeup-exams/${id}/grade`,
  MAKEUP_BY_ID: (id) => `${API_BASE_URL}/api/makeup-exams/${id}`,
  MAKEUP_BY_STUDENT: (id) => `${API_BASE_URL}/api/makeup-exams/student/${id}`,
  MAKEUP_BY_STUDENT_NUMBER: (studentNumber) => `${API_BASE_URL}/api/makeup-exams/student/number/${studentNumber}`,
  MAKEUP_BY_TEACHER: `${API_BASE_URL}/api/makeup-exams/teacher`,
  MAKEUP_STUDENT_SEMESTER: (id, semester) => `${API_BASE_URL}/api/makeup-exams/student/${id}/semester/${semester}`,
  MAKEUP_STUDENT_NUMBER_SEMESTER: (studentNumber, semester) => `${API_BASE_URL}/api/makeup-exams/student/number/${studentNumber}/semester/${semester}`,
  MAKEUP_TEACHER_COURSE_SEMESTER: (courseCode, semester) => `${API_BASE_URL}/api/makeup-exams/teacher/course/${courseCode}/semester/${semester}`,
  MAKEUP_PENDING: `${API_BASE_URL}/api/makeup-exams/pending`,
  MAKEUP_DELETE: (id) => `${API_BASE_URL}/api/makeup-exams/${id}`,
  MAKEUP_MY: `${API_BASE_URL}/api/makeup-exams/my`,
   
   // Tuition
   TUITION: `${API_BASE_URL}/api/tuition`,
   TUITION_BY_ID: (id) => `${API_BASE_URL}/api/tuition/${id}`,
   TUITION_BY_STUDENT: (id) => `${API_BASE_URL}/api/tuition/student/${id}`,
   TUITION_BY_STUDENT_NUMBER: (studentNumber) => `${API_BASE_URL}/api/tuition/student/number/${studentNumber}`,
   TUITION_BY_SEMESTER: (semester) => `${API_BASE_URL}/api/tuition/semester/${semester}`,
   TUITION_BY_STUDENT_AND_SEMESTER: (id, semester) => `${API_BASE_URL}/api/tuition/student/${id}/semester/${semester}`,
   TUITION_BY_STUDENT_NUMBER_AND_SEMESTER: (studentNumber, semester) => `${API_BASE_URL}/api/tuition/student/number/${studentNumber}/semester/${semester}`,
   TUITION_ALL: `${API_BASE_URL}/api/tuition/all`,
   
   // Scholarship
   SCHOLARSHIP: `${API_BASE_URL}/api/scholarships`,
   SCHOLARSHIP_BY_ID: (id) => `${API_BASE_URL}/api/scholarships/${id}`,
   SCHOLARSHIP_BY_STUDENT: (id) => `${API_BASE_URL}/api/scholarships/student/${id}`,
   SCHOLARSHIP_BY_STUDENT_NUMBER: (studentNumber) => `${API_BASE_URL}/api/scholarships/student/number/${studentNumber}`,
   SCHOLARSHIP_BY_SEMESTER: (semester) => `${API_BASE_URL}/api/scholarships/semester/${semester}`,
   SCHOLARSHIP_BY_STUDENT_AND_SEMESTER: (id, semester) => `${API_BASE_URL}/api/scholarships/student/${id}/semester/${semester}`,
   SCHOLARSHIP_BY_STUDENT_NUMBER_AND_SEMESTER: (studentNumber, semester) => `${API_BASE_URL}/api/scholarships/student/number/${studentNumber}/semester/${semester}`,
   
   // Payment Records
   PAYMENT_RECORDS: `${API_BASE_URL}/api/payment-records`,
   PAYMENT_RECORD_BY_ID: (id) => `${API_BASE_URL}/api/payment-records/${id}`,
   PAYMENT_RECORDS_BY_STUDENT: (id) => `${API_BASE_URL}/api/payment-records/student/${id}`,
   PAYMENT_RECORDS_BY_STUDENT_NUMBER: (studentNumber) => `${API_BASE_URL}/api/payment-records/student/number/${studentNumber}`,
   PAYMENT_RECORDS_BY_SEMESTER: (semester) => `${API_BASE_URL}/api/payment-records/semester/${semester}`,
   PAYMENT_RECORDS_BY_STUDENT_AND_SEMESTER: (id, semester) => `${API_BASE_URL}/api/payment-records/student/${id}/semester/${semester}`,
   PAYMENT_RECORDS_BY_STUDENT_NUMBER_AND_SEMESTER: (studentNumber, semester) => `${API_BASE_URL}/api/payment-records/student/number/${studentNumber}/semester/${semester}`,
   
   // Appointments
   APPOINTMENTS: `${API_BASE_URL}/api/appointments`,
   APPOINTMENT_BY_ID: (id) => `${API_BASE_URL}/api/appointments/${id}`,
   APPOINTMENTS_BY_STUDENT: (id) => `${API_BASE_URL}/api/appointments/student/${id}`,
   APPOINTMENTS_BY_STUDENT_NUMBER: (studentNumber) => `${API_BASE_URL}/api/appointments/student/number/${studentNumber}`,
   APPOINTMENTS_MINE: `${API_BASE_URL}/api/appointments/mine`
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