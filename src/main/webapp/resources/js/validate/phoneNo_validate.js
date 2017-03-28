
function checkPhone(phone){ 
	if(!(/^1[34578]\d{9}$/.test(phone))){ 
		return false; 
		}
	return true;
	}