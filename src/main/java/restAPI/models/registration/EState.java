package restAPI.models.registration;

public enum EState {
    STATE_UNAUTHENTICATED,  // moi dang ky, chua duoc xac thuc
    STATE_AUTHENTICATED,    // chinh quyen da xac thuc, ward chua bi lu lut
    STATE_SAFE,             // da xac thuc, ward dang lu lut, TNV da goi va xac nhan an toan
    STATE_DANGER,           // da xac thuc, ward dang lu lut, TNV chua goi
    STATE_EMERGENCY,        // da xac thuc, ward dang lu lut, TNV da goi va can cau cuu
    STATE_SAVED,             // da xac thuc, ward dang lu lut, doi cuu ho da cuu

    STATE_REJECT            // chi su dung cho dang ky chinh quyen
}
